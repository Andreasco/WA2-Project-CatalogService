package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderProduct
import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderRequest
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderResponseDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderStatus
import it.polito.wa2project.wa2projectcatalogservice.dto.toOrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.coreography.OrderRequestRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.persistence.OneToMany


@Service
class ChoreographyCatalogService( val kafkaTemplate: KafkaTemplate<String, OrderRequestDTO>,
                                  val orderRequestRepository: OrderRequestRepository) {

    @KafkaListener(topics = arrayOf("orderSagaResponse"), groupId = "group1")
    fun loadOrderSagaResponse( orderResponseDTO: OrderResponseDTO) {
        try{
            println(orderResponseDTO)
        } catch (e: Exception){
            println("Exception on KafkaListener")
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }

    @Transactional
    fun createOrder( orderRequest: OrderRequestDTO): OrderRequestDTO{
        val newOrderRequest =
            OrderRequest(
                orderRequest.orderId,
                orderRequest.buyerId,
                orderRequest.deliveryName,
                orderRequest.deliveryStreet,
                orderRequest.deliveryZip,
                orderRequest.deliveryCity,
                orderRequest.deliveryNumber,
                orderRequest.status,
                orderRequest.totalPrice,
                orderRequest.destinationWalletId,
                orderRequest.sourceWalletId,
                orderRequest.transactionReason,
                orderRequest.reasonDetail)

        orderRequest.orderProducts.forEach{
            newOrderRequest.addOrderProduct( OrderProduct(null,
                it.purchasedProductId,
                it.quantity,
                it.purchasedProductPrice,
                it.warehouseId )
            )}

        return orderRequestRepository.save(newOrderRequest).toOrderRequestDTO()
    }

    fun sendOrderRequestDTO(orderRequestDTO: OrderRequestDTO) {


        val future: ListenableFuture<SendResult<String, OrderRequestDTO>> = kafkaTemplate.send("orderSagaRequest", orderRequestDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderRequestDTO>> {
            override fun onSuccess(result: SendResult<String, OrderRequestDTO>?) {
                println("Sent message orderRequestDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message orderRequestDTO")
            }
        })
    }
}