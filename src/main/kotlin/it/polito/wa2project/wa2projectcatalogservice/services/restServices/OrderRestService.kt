package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderRequest
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderStatus
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import it.polito.wa2project.wa2projectcatalogservice.repositories.coreography.OrderRequestRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class OrderRestService(
    restTemplateBuilder: RestTemplateBuilder,
    val userRepository: UserRepository,
    val warehouseRestService: WarehouseRestService,
    val walletRestService: WalletRestService,
    val orderRequestRepository: OrderRequestRepository
    ) {

    private val restTemplate: RestTemplate

    private val orderServiceURL = "http://localhost:8200/orderservice/orders"

    init {
        restTemplate = restTemplateBuilder.build()
    }

    fun getAllOrders(): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val url = "$orderServiceURL?buyerId=$userId"
        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET ALL ORDERS: Order service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getAllOrdersAdmin(userId: Long): ResponseEntity<String>{
        val url = "$orderServiceURL?buyerId=$userId"
        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET ALL ORDERS ADMIN: Order service response $responseEntity")

        return responseEntity
    }

    fun getOrder(orderId: Long): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val url = "$orderServiceURL/$orderId?buyerId=$userId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET ORDER: Order service response $responseEntity")

        return responseEntity
    }

    fun updateOrder(newOrder: OrderDTO, orderId: Long): ResponseEntity<String>{
        val url = "$orderServiceURL/$orderId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newOrder, headers)

        //Send PATCH request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PATCH, entity)

        println("EDIT PRODUCT: Order service response $responseEntity")

        return responseEntity
    }

    fun deleteOrder(orderId: Long): ResponseEntity<String> {
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val orderRequest = orderRequestRepository.findByOrderId(orderId)
            ?: return ResponseEntity("There's no order with such ID", HttpStatus.NOT_FOUND)

        if(orderRequest.buyerId != userId)
            return ResponseEntity("You can't delete this order because you haven't bought it", HttpStatus.FORBIDDEN)

        if(orderRequest.status == OrderStatus.CANCELED)
            return ResponseEntity("You can't delete this order because you have already deleted it", HttpStatus.FORBIDDEN)

        var deleted = false

        var i = 0
        while (i < 5 && !deleted) {
            /*val step1 = reloadWarehouse(orderRequest)
            if(!step1) {
                //First step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                Thread.sleep(5000)
                continue
            }

            val step2 = refundUser(orderRequest)
            if(!step2) {
                //Second step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                Thread.sleep(5000)
                continue
            }

            val step3 = deleteOrderFromService(orderRequest)
            if(!step3) {
                //Third step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                undoRefundUser(orderRequest)
                Thread.sleep(5000)
                continue
            }*/

            try{
                reloadWarehouse(orderRequest)
            }
            catch(e: Exception) {
                //First step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                Thread.sleep(5000)
                i++
                continue
            }

            try{
                refundUser(orderRequest)
            }
            catch(e: Exception){
                //Second step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                Thread.sleep(5000)
                i++
                continue
            }

            try{
                deleteOrderFromService(orderRequest)
                deleted = true
            }
            catch(e: Exception){
                //Third step failed so undo what I have done, wait 5 seconds and retry
                unloadWarehouse(orderRequest)
                undoRefundUser(orderRequest)
                Thread.sleep(5000)
                i++
                continue
            }
        }

        println("DELETE ORDER: Deletion completed: $deleted")

        return if (deleted) {
            orderRequest.status = OrderStatus.CANCELED
            orderRequestRepository.save(orderRequest)

            ResponseEntity("Your order has been deleted", HttpStatus.OK)
        }
        else
            ResponseEntity("We couldn't delete your order, try again", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /*private fun reloadWarehouse(orderRequest: OrderRequest): Boolean{
        val response = warehouseRestService.reloadProduct(orderRequest.uuid)

        return response.statusCode == HttpStatus.OK
    }

    private fun unloadWarehouse(orderRequest: OrderRequest){
        warehouseRestService.undoReloadProduct(orderRequest.uuid)

        //Assuming that everything goes well calling this function
        //return response.statusCode != HttpStatus.OK
    }

    private fun refundUser(orderRequest: OrderRequest): Boolean{
        val response = walletRestService.refundWallet(orderRequest.uuid)

        return response.statusCode == HttpStatus.OK
    }

    private fun undoRefundUser(orderRequest: OrderRequest): Boolean{
        val response = walletRestService.undoRefundUser(orderRequest.uuid)

        return response.statusCode == HttpStatus.OK
    }*/

    private fun reloadWarehouse(orderRequest: OrderRequest){
        warehouseRestService.reloadProduct(orderRequest.uuid)
    }

    private fun unloadWarehouse(orderRequest: OrderRequest){
        warehouseRestService.undoReloadProduct(orderRequest.uuid)

        //Assuming that everything goes well calling this function
        //return response.statusCode != HttpStatus.OK
    }

    private fun refundUser(orderRequest: OrderRequest){
        walletRestService.refundWallet(orderRequest.uuid)
    }

    private fun undoRefundUser(orderRequest: OrderRequest){
        walletRestService.undoRefundUser(orderRequest.uuid)
    }

    private fun deleteOrderFromService(orderRequest: OrderRequest): Boolean{
        val orderId = orderRequest.orderId
        val userId = orderRequest.buyerId

        val orderServiceUrl = "$orderServiceURL/$orderId?buyerId=$userId"

        //Send DELETE request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(orderServiceUrl, HttpMethod.DELETE)

        println("DELETE ORDER FROM SERVICE: Order service response $responseEntity")

        return responseEntity.statusCode == HttpStatus.OK
    }
}
