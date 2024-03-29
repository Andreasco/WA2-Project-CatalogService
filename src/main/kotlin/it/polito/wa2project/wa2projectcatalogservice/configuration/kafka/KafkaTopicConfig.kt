package it.polito.wa2project.wa2projectcatalogservice.configuration.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin


@Configuration
class KafkaTopicConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun orderSagaResponseTopic(): NewTopic {
        return NewTopic("orderCatalogSagaRequest", 1, 1.toShort())
    }

    @Bean
    fun emailRequestTopic(): NewTopic {
        return NewTopic("emailRequest", 1, 1.toShort())
    }

    /**
    @Bean
    fun orderSagaRequestTopic(): NewTopic {
        return NewTopic("orderSagaRequest", 1, 1.toShort())
    }

    */
}
