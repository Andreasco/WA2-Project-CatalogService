package it.polito.wa2project.wa2projectcatalogservice.configuration.kafka

import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderResponseDTO
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class KafkaProducerConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Bean
    fun producerFactory(): ProducerFactory<String, OrderRequestDTO> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        // configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        // configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps, StringSerializer(), JsonSerializer())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, OrderRequestDTO> {
        return KafkaTemplate(producerFactory())
    }
}