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
@RequestMapping("/orders")
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
        @RequestParam
        @Positive(message = "Insert a valid userID")
        userId: Long? = null
    ): ResponseEntity<String> {
        return if(userId != null)
            orderRestService.getAllOrdersAdmin(userId)
        else
            orderRestService.getAllOrders()
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<String> {

        return orderRestService.getOrder(orderId)
    }

    @PatchMapping("/{orderId}")
    fun updateOrder(
        @RequestBody
        @NotNull(message = "Insert a valid order")
        newOrder: OrderDTO,

        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<String> {

        return orderRestService.updateOrder(newOrder, orderId)
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<String> {

        return orderRestService.deleteOrder(orderId)
    }
}
