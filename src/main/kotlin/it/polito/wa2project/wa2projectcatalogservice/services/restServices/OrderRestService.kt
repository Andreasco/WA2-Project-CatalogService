package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class OrderRestService(restTemplateBuilder: RestTemplateBuilder, userRepository: UserRepository) {
    private val restTemplate: RestTemplate

    private val orderServiceURL = "http://localhost:8200/orderService/orders" //TODO cambia la porta?
    private val userId: Long

    init {
        restTemplate = restTemplateBuilder.build()
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        userId = userRepository.findByUsername(usernameLogged)!!.getId()!!
    }

    fun getAllOrders(): String{
        val url = "$orderServiceURL?buyerId=$userId"
        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET ALL ORDERS: Order service response $response")

        return response
    }

    fun getOrder(orderId: Long): String{
        //TODO Guido deve aggiungere il buyerId anche in questo caso altrimenti chiunque potrebbe richiedere qualunque ordine
        val url = "$orderServiceURL/$orderId?buyerId=$userId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET ORDER: Order service response $response")

        return response
    }

    fun updateOrder(newOrder: OrderDTO, orderId: Long): OrderDTO{
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
        val response: OrderDTO = restTemplate.patchForObject(url, entity)

        println("EDIT PRODUCT: Order service response $response")

        return response
    }

    fun deleteOrder(orderId: Long): HttpStatus {
        val url = "$orderServiceURL/$orderId?buyerId=$userId"

        //Send DELETE request
        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)
        val responseStatusCode = response.statusCode

        println("DELETE ORDER: Order service response $response")

        return responseStatusCode
    }
}
