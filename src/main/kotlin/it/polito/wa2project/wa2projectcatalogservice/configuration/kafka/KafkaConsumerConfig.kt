package it.polito.wa2project.wa2projectcatalogservice.configuration.kafka

import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderResponseDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.NotificationRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import it.polito.wa2project.wa2projectcatalogservice.services.ChoreographyCatalogService
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.NotificationRestService
import it.polito.wa2project.wa2projectcatalogservice.services.UserDetailsServiceImpl
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class KafkaConsumerConfig(
    val choreographyCatalogService: ChoreographyCatalogService,
    val userDetailsService: UserDetailsServiceImpl,
    val notificationRestService: NotificationRestService,
    val userRepository: UserRepository
    ) {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${spring.kafka.consumer.group-id}")
    private val groupId: String? = null

    val orderErrorCodes = mapOf(
        -1 to "of an internal error.",
        1 to "there are not have enough funds in your wallet.",
        2 to "there are not enough items in stock."
        //TODO vedere cosa manca
    )

    val orderStatusCodes = mapOf(
        0 to "request has been received.",
        1 to "has been sent."
        //TODO vedere cosa manca
    )

    @Bean
    fun consumerFactoryOrder(): ConsumerFactory<String, OrderResponseDTO> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        // props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        // props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        val errorHandlingDeserializer: ErrorHandlingDeserializer<OrderResponseDTO> = ErrorHandlingDeserializer(
            JsonDeserializer(OrderResponseDTO::class.java).trustedPackages("*").ignoreTypeHeaders()
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), errorHandlingDeserializer)

    }

    @Bean
    fun kafkaListenerContainerFactoryOrder(): ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO>()
        factory.consumerFactory = consumerFactoryOrder()
        return factory
    }

    @Bean
    fun consumerFactoryNotification(): ConsumerFactory<String, NotificationRequestDTO> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress!!
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId!!
        // props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        // props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        val errorHandlingDeserializer: ErrorHandlingDeserializer<NotificationRequestDTO> = ErrorHandlingDeserializer(
            JsonDeserializer(NotificationRequestDTO::class.java).trustedPackages("*").ignoreTypeHeaders()
        )
        return DefaultKafkaConsumerFactory(props, StringDeserializer(), errorHandlingDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactoryNotification(): ConcurrentKafkaListenerContainerFactory<String, NotificationRequestDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, NotificationRequestDTO>()
        factory.consumerFactory = consumerFactoryNotification()
        return factory
    }

    //Nota, il primo listener gestisce errori mentre il secondo ordini andati a buon fine

    @KafkaListener(topics = ["orderWarehouseSagaResponse"], groupId = "group1", containerFactory = "kafkaListenerContainerFactoryOrder")
    fun receiveWarehouseResponse(orderResponseDTO: OrderResponseDTO) {
        println("OrderResponse arrived from warehouseService: $orderResponseDTO")

        if (orderResponseDTO.exitStatus != -2L) {  //if exitStatus == -2 it means that the order has been duplicated, probably by Debezium
            val errorCode = orderResponseDTO.exitStatus.toInt()
            val emailText =
                "Hello, we are sorry to inform you that we could not process your order because ${orderErrorCodes[errorCode]}"
            val subject = "Order error"

            choreographyCatalogService.rollbackOrder(orderResponseDTO.uuid)
            sendOrderUpdateEmail(orderResponseDTO, subject, emailText)
        }
    }

    @KafkaListener(topics = ["orderWalletSagaResponse"], groupId = "group1", containerFactory = "kafkaListenerContainerFactoryOrder")
    fun receiveOrderResponse(orderResponseDTO: OrderResponseDTO) {
        println("OrderResponse arrived from walletService: $orderResponseDTO")

        //Aggiorno orderRequest con l'orderId di orderResponse
        choreographyCatalogService.addIdToOrderRequest(orderResponseDTO)

        val statusCode = orderResponseDTO.exitStatus.toInt()
        val orderId = orderResponseDTO.orderId
        val emailText = "Hello, we are glad to inform you that your order with ID $orderId ${orderStatusCodes[statusCode]}"
        val subject = "Order status update"

        sendOrderUpdateEmail(orderResponseDTO, subject, emailText)
    }

    private fun sendOrderUpdateEmail(orderResponseDTO: OrderResponseDTO, subject: String, emailText: String){
        val orderDTO = choreographyCatalogService.getOrderByUuid(orderResponseDTO.uuid)
        val buyerEmail = userDetailsService.getUserByIdInternal(orderDTO.buyerId!!).email!!

        notificationRestService.sendEmail(buyerEmail, subject, emailText)
    }

    @KafkaListener(topics = ["emailRequest"], groupId = "group1", containerFactory = "kafkaListenerContainerFactoryNotification")
    fun receiveWarehouseEmailRequest(notificationRequestDTO: NotificationRequestDTO) {
        println("NotificationRequestDTO arrived: $notificationRequestDTO")

        val subject = notificationRequestDTO.messageObject
        val emailText = notificationRequestDTO.message

        if (notificationRequestDTO.userId == null) {
            val adminsEmails = userRepository.findAdminsEmails()
            adminsEmails.forEach { email -> notificationRestService.sendEmail(email, subject, emailText) }
        }
        else{
            val user = userRepository.findById(notificationRequestDTO.userId).get()
            notificationRestService.sendEmail(user.email, subject, emailText)
        }
    }
}
