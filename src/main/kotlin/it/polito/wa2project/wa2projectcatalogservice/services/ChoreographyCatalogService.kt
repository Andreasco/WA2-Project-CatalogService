package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderProduct
import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderRequest
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderResponseDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderStatus
import it.polito.wa2project.wa2projectcatalogservice.dto.order.toOrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import it.polito.wa2project.wa2projectcatalogservice.repositories.coreography.OrderRequestRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback


@Service
@Transactional
class ChoreographyCatalogService(
    val kafkaTemplate: KafkaTemplate<String, OrderRequestDTO>,
    val orderRequestRepository: OrderRequestRepository,
    val userRepository: UserRepository
    ) {

    fun getOrderByUuid(orderRequestUuid: String): OrderRequestDTO {
        return orderRequestRepository.findByUuid(orderRequestUuid)!!.toOrderRequestDTO()
    }

    fun createOrder(orderRequest: OrderRequestDTO): OrderRequestDTO {
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()

        val newOrderRequest = OrderRequest(
            orderRequest.orderId,
            //orderRequest.buyerId, //for tests only
            userId,
            orderRequest.deliveryName,
            orderRequest.deliveryStreet,
            orderRequest.deliveryZip,
            orderRequest.deliveryCity,
            orderRequest.deliveryNumber,
            orderRequest.status,
            orderRequest.totalPrice,
            orderRequest.destinationWalletId ?: 1,
            orderRequest.sourceWalletId,
            orderRequest.transactionReason,
        )

        orderRequest.orderProducts.forEach{
            newOrderRequest.addOrderProduct( OrderProduct(null,
                it.purchasedProductId,
                it.quantity,
                it.purchasedProductPrice,
                it.warehouseId )
            )
        }

        return orderRequestRepository.save(newOrderRequest).toOrderRequestDTO()
    }

    fun sendOrderRequestDTO(orderRequestDTO: OrderRequestDTO) {
        val future: ListenableFuture<SendResult<String, OrderRequestDTO>> = kafkaTemplate.send("orderCatalogSagaRequest", orderRequestDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderRequestDTO>> {
            override fun onSuccess(result: SendResult<String, OrderRequestDTO>?) {
                println("Sent message orderRequestDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message orderRequestDTO")
            }
        })
    }

    fun rollbackOrder(uuid: String){
        val orderRequest = orderRequestRepository.findByUuid(uuid)!!

        orderRequest.status = OrderStatus.FAILED

        orderRequestRepository.save(orderRequest)
    }

    fun addIdToOrderRequest(orderResponseDTO: OrderResponseDTO){
        val orderRequest = orderRequestRepository.findByUuid(orderResponseDTO.uuid)
        orderRequest!!.orderId = orderResponseDTO.orderId

        orderRequestRepository.save(orderRequest)
    }
}
