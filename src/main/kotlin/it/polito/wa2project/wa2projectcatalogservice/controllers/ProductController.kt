package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.product.CommentDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.ProductDTO
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.ProductRestService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/products")
@Validated
class ProductController(val productRestService: ProductRestService) {
    @PostMapping("/{productId}/comments")
    fun postComment(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid comment")
        commentDTO: CommentDTO
    ): ResponseEntity<String> {

        return productRestService.postComment(commentDTO, productId)
    }

    @PostMapping
    fun addProduct(
        @RequestBody
        @NotNull(message = "Insert a valid product")
        product: ProductDTO
    ): ResponseEntity<String> {

        return productRestService.addProduct(product)
    }

    @PutMapping("/{productId}")
    fun updateOrCreateProduct(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull
        productDTO: ProductDTO
    ): ResponseEntity<String> {
        return productRestService.updateOrCreateProduct(productId, productDTO)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,
    ): ResponseEntity<String> {

        return productRestService.deleteProduct(productId)
    }

    @PatchMapping("/{productId}")
    fun editProduct(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid product")
        newProduct: ProductDTO
    ): ResponseEntity<String> {

        return productRestService.editProduct(newProduct, productId)
    }

    @PostMapping("/{productId}/picture")
    fun uploadProductPicture(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid picture")
        pictureUrl: String
    ): ResponseEntity<String> {

        return productRestService.uploadProductPicture(pictureUrl, productId)
    }

    @GetMapping("/{productId}/warehouses")
    fun getProductWarehouses(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return productRestService.getProductWarehouses(productId)
    }

    @GetMapping("/{productId}/storages")
    fun getProductStorages(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return productRestService.getProductStorages(productId)
    }
}
