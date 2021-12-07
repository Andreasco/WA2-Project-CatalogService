package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.UserDetailsDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.UserRegistrationBodyDTO
import it.polito.wa2project.wa2projectcatalogservice.services.ChoreographyCatalogService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Validated
class CatalogController(val choreographyCatalogService: ChoreographyCatalogService) {

    @PostMapping("/order")
    fun addOrder(
        @RequestBody
        orderRequestDTO: OrderRequestDTO
    ): ResponseEntity<String> {
        choreographyCatalogService.createOrder(orderRequestDTO)
        return ResponseEntity(HttpStatus.OK)
    }
}