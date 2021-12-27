package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.services.ChoreographyCatalogService
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.OrderRestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/order")
@Validated
class OrderController(val choreographyCatalogService: ChoreographyCatalogService,
                      val orderRestService: OrderRestService
) {

    @PostMapping
    fun addOrder(
        @RequestBody
        orderRequestDTO: OrderRequestDTO
    ): ResponseEntity<String> {
        choreographyCatalogService.createOrder(orderRequestDTO)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping
    fun getAllOrders(
    ): ResponseEntity<String> {
        val ordersList = orderRestService.getAllOrders()

        return ResponseEntity(ordersList, HttpStatus.OK)
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<String> {
        val order = orderRestService.getOrder(orderId)

        return ResponseEntity(order, HttpStatus.OK)
    }

    @PatchMapping("/{orderId}")
    fun updateOrder(
        @RequestBody
        @NotNull(message = "Insert a valid order")
        newOrder: OrderDTO,

        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<OrderDTO> {
        val updatedOrder = orderRestService.updateOrder(newOrder, orderId)

        return ResponseEntity(updatedOrder, HttpStatus.OK)
    }

    //TODO anche questo deve essere gestito con una saga?
    @DeleteMapping("/{orderId}")
    fun deleteOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<Any> {
        val responseStatusCode = orderRestService.deleteOrder(orderId)

        return ResponseEntity(responseStatusCode)
    }
}
