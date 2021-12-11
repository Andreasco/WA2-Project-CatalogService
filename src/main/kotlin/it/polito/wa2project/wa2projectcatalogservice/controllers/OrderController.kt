package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.order.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.services.ChoreographyCatalogService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/order")
@Validated
class OrderController(val choreographyCatalogService: ChoreographyCatalogService) {

    @PostMapping()
    fun addOrder(
        @RequestBody
        orderRequestDTO: OrderRequestDTO
    ): ResponseEntity<String> {
        choreographyCatalogService.createOrder(orderRequestDTO)
        return ResponseEntity(HttpStatus.OK)
    }

    /*//TODO devo aggiungere quindi un OrderDTO? Provare altrimenti a inoltrare semplicemente il json che mi arriva
    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<OrderDTO> {


        return ResponseEntity(orderDTO, HttpStatus.OK)
    }

    @PutMapping("/cancel/{orderId}")
    fun cancelOrder(
        @PathVariable
        @Positive(message = "Insert a valid orderId")
        orderId: Long
    ): ResponseEntity<OrderDTO> {

        //Return the order that I've just canceled
        return ResponseEntity(orderDTO, HttpStatus.OK)
    }*/
}
