package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.services.restServices.ProductRestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/marketplace")
class MarketplaceController(val productRestService: ProductRestService) {

    @GetMapping("/products")
    fun getProductsByCategory(
        @RequestParam
        category: String? = null,
    ): ResponseEntity<String> { //Dovrebbe contenere il JSON che mi manda warehouse

        return productRestService.getProductsByCategory(category)
    }

    @GetMapping("/products/{productId}")
    fun getProductInfo(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return productRestService.getProductInfo(productId)
    }

    @GetMapping("/products/{productId}/picture")
    fun getProductPicture(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return productRestService.getProductPicture(productId)
    }

    @GetMapping("/products/{productId}/comments")
    fun getProductComments(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return productRestService.getProductComments(productId)
    }
}
