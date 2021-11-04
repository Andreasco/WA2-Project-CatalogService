/*

import io.debezium.data.Envelope
import io.debezium.embedded.Connect
import io.debezium.engine.DebeziumEngine
import io.debezium.engine.RecordChangeEvent
import io.debezium.engine.format.ChangeEventFormat
import it.polito.wa2project.wa2projectcatalogservice.services.ChoreographyCatalogService
import it.polito.wa2project.wa2projectcatalogservice.services.DbOperation
import org.apache.commons.lang3.tuple.Pair
import org.apache.kafka.connect.data.Field
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Function
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


/**
 * This class creates, starts and stops the EmbeddedEngine, which starts the Debezium engine. The engine also
 * loads and launches the connectors setup in the configuration.
 *
 *
 * The class uses @PostConstruct and @PreDestroy functions to perform needed operations.
 *
 */
@Component
class OutboxListener private constructor(val orderRequestConnector: io.debezium.config.Configuration, val coreographyCatalogService: ChoreographyCatalogService) {


    /**
     * Single thread pool which will run the Debezium engine asynchronously.
     */
    private val executor: Executor = Executors.newSingleThreadExecutor()

    /**
     * The Debezium engine which needs to be loaded with the configurations, Started and Stopped - for the
     * CDC to work.
     */
    private val engine: DebeziumEngine<RecordChangeEvent<SourceRecord>>?

    /**
     * Constructor which loads the configurations and sets a callback method 'handleEvent', which is invoked when
     * a DataBase transactional operation is performed.
     *
     */
    init {
        println("[+++++++++++++++++] OutboxListener Init \n \n \n \n \n \n \n")
        this.engine = DebeziumEngine.create(
            ChangeEventFormat.of(
                Connect::class.java
            )
        )
            .using(orderRequestConnector.asProperties())
            .notifying(this::handleEvent)
            .build()
    }

    /**
     * The method is called after the Debezium engine is initialized and started asynchronously using the Executor.
     */
    @PostConstruct
    private fun start() {
        println("OutboxListener started")
        executor.execute(engine)
    }

    /**
     * This method is called when the container is being destroyed. This stops the debezium, merging the Executor.
     */
    @PreDestroy
    private fun stop() {
        if (this.engine != null) {
            this.engine.close()
        }
    }

    /**
     * This method is invoked when a transactional action is performed on any of the tables that were configured.
     *
     * @param sourceRecord
     */
    private fun handleEvent(sourceRecordRecordChangeEvent: RecordChangeEvent<SourceRecord> ) {
        println( "OrderRequest Event occurred")
        val sourceRecord: SourceRecord = sourceRecordRecordChangeEvent.record()
        val sourceRecordValue = sourceRecord.value() as Struct
        if (sourceRecordValue != null) {
            val operation: DbOperation = DbOperation.forCode(sourceRecordValue[Envelope.FieldName.OPERATION] as String)!!

            //Only if this is a transactional operation.
            if (operation !== DbOperation.READ) {
                val message: Map<String, Any?>
                var record = Envelope.FieldName.AFTER //For Update & Insert operations.
                if (operation === DbOperation.DELETE) {
                    record = Envelope.FieldName.BEFORE //For Delete operations.
                }

                //Build a map with all row data received.
                val struct = sourceRecordValue[record] as Struct
                message = struct.schema().fields().stream()
                    .map { obj: Field -> obj.name() }
                    .filter { fieldName: String? -> struct[fieldName] != null }
                    .map { fieldName: String ->
                        Pair.of(
                            fieldName,
                            struct[fieldName]
                        )
                    }
                    .collect(
                        Collectors.toMap(
                            Function { (key): Pair<String, Any?> -> key },
                            Function { (_, value): Pair<String, Any?> -> value })
                    )

                //Call the service to handle the data change.
                if( DbOperation.CREATE.name == operation.name) {
                    print("Data Changed: {${message}} with Operation: {${operation.name}}, creating message")
                    //coreographyCatalogService.sendOrderRequestDTO(message)
                }
                else
                    print("Data Changed: {${message}} with Operation: {${operation.name}}, no action provided")
            }
        }
    }


}

 */