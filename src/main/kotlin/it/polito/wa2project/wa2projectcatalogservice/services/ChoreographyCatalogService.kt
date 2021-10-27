package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderResponseDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ChoreographyCatalogService {

    @KafkaListener(topics = arrayOf("orderSagaResponse"), groupId = "group1")
    fun loadOrderSagaResponse( orderResponseDTO: OrderResponseDTO) {
        try{
            println(orderResponseDTO)
        } catch (e: Exception){
            println("Exception on KafkaListener")
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }

    fun createOrder( request: OrderRequestDTO ){
        // TODO Fill Outbox Table
    }
}