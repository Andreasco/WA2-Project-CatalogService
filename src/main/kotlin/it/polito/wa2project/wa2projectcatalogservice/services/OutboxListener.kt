package it.polito.wa2project.wa2projectcatalogservice.services

import io.debezium.data.Envelope
import io.debezium.embedded.Connect
import io.debezium.engine.DebeziumEngine
import io.debezium.engine.RecordChangeEvent
import io.debezium.engine.format.ChangeEventFormat
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderStatus
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.tuple.Pair
import org.apache.kafka.connect.data.Field
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.stream.Collectors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Slf4j
@Component
class OutboxListener(
    val orderRequestConnector: io.debezium.config.Configuration,
    val choreographyCatalogService: ChoreographyCatalogService
    ) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val debeziumEngine: DebeziumEngine<RecordChangeEvent<SourceRecord>>?

    init {
        println("[+++++++++++++++++] OutboxListener Init \n \n \n \n \n \n \n")

        debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect::class.java))
            .using(orderRequestConnector.asProperties())
            .notifying { sourceRecordRecordChangeEvent: RecordChangeEvent<SourceRecord> ->
                handleChangeEvent(
                    sourceRecordRecordChangeEvent
                )
            }
            .build()
    }

    @PostConstruct
    private fun start() {
        println("[+++++++++++++++++] OutboxListener Init \n \n \n \n \n \n \n")

        executor.execute(debeziumEngine) //DebeziumEngine extends Runnable so it's fine
    }

    private fun handleChangeEvent(sourceRecordRecordChangeEvent: RecordChangeEvent<SourceRecord>) {
        println("[+++++++++++++++++] OutboxListener Changed \n \n \n \n \n \n \n")
        val sourceRecord = sourceRecordRecordChangeEvent.record()
        val sourceRecordChangeValue = sourceRecord.value() as Struct
        if (sourceRecordChangeValue != null) {
            val operation = Envelope.Operation.forCode(sourceRecordChangeValue[Envelope.FieldName.OPERATION] as String)
            if (operation != Envelope.Operation.READ) {
                val record =
                    if (operation == Envelope.Operation.DELETE) // Handling Update & Insert operations.
                        Envelope.FieldName.BEFORE
                    else
                        Envelope.FieldName.AFTER
                if (operation == Envelope.Operation.CREATE) {
                    val struct = sourceRecordChangeValue[record] as Struct

                    val payload = struct.schema().fields().stream()
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
                                { (key): Pair<String, Any?> -> key },
                                { (_, value): Pair<String, Any?> -> value }
                            )
                        )

                    //NOTA: i campi sono scritti tutti in minuscolo col _ al posto della maiuscola,
                    // quindi payload["buyer_id"] per esempio

                    //Se orderRequest cambia aggiungendo l'orderId o mettendolo a FAILED allora non devo fare niente
                    if(payload["order_id"] != null || payload["order_status"] == OrderStatus.FAILED)
                        return

                    val orderRequestDTO = choreographyCatalogService.getOrderByUuid(payload["uuid"] as String)

                    println("OrderRequestDTO from DB on CREATE:\n$orderRequestDTO")

                    choreographyCatalogService.sendOrderRequestDTO(orderRequestDTO)
                }
            }
        }
    }

    @PreDestroy
    @Throws(IOException::class)
    private fun stop() {
        debeziumEngine?.close()
    }
}
