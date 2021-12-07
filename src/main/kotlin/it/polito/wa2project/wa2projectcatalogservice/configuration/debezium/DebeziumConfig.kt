package it.polito.wa2project.wa2projectcatalogservice.configuration.debezium

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DebeziumConfig {

    @Bean
    fun orderRequestConnector(): io.debezium.config.Configuration? {
        return io.debezium.config.Configuration.create()
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "./cdc/offset/request-offset.dat")
            .with("offset.flush.interval.ms", 60000)
            .with("name", "order-request-connector")
            //.with("database.server.name", studentDBHost.toString() + "-" + studentDBName)
            .with("database.hostname", "localhost")
            .with("database.port", "5432")
            .with("database.user", "guido")
            .with("database.password", "password")
            .with("database.dbname", "catalogServiceDB")
            .with("database.include.list", "catalogServiceDB")
            .with("include.schema.changes", "false")
            .with("database.server.id", "10181")
            .with("database.server.name", "orderrequest-mysql-db-server")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .with("database.history.file.filename", "./tmp/dbhistory.dat")
            //.with("table.whitelist", "order_request")
            .with("database.history.skip.unparseable.ddl", true)
            .with("database.allowPublicKeyRetrieval", true)
            .build()
    }
}