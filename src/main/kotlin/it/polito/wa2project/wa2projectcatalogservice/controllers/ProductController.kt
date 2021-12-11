package it.polito.wa2project.wa2projectcatalogservice.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/product")
@Validated
class ProductController {

    /*//TODO come faccio per prenderne più di uno? Lista di ID?
    @GetMapping("/{productId}")
    fun getProduct(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<ProductDTO> {

        return ResponseEntity(productDTO, HttpStatus.OK)
    }*/
}
