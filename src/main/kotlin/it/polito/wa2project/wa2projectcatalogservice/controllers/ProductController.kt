package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.ProductDTO
import it.polito.wa2project.wa2projectcatalogservice.services.ProductRestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/product")
@Validated
class ProductController(val productRestService: ProductRestService) {
    //TODO devo controllare se il prodotto per cui vogliono fare una operazione è stato comprato dall'utente
    //uso una chiamata rest verso orderService oppure controllo nella mia tabella orderRequest?

    //TODO controllare se mettere ProductDTO al posto di Any (any potrebbe servire nel caso fosse possibile
    //semplicemente inoltrare la risposta del warehouse)
    @PostMapping("/{productId}/stars")
    fun giveStars(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @Positive(message = "Insert a valid number of stars")
        stars: Int
    ): ResponseEntity<String> {
        val newProduct = productRestService.giveStars(stars, productId)

        return ResponseEntity(newProduct, HttpStatus.OK)
    }

    //TODO controllare se mettere ProductDTO al posto di Any (any potrebbe servire nel caso fosse possibile
    //semplicemente inoltrare la risposta del warehouse)
    @PostMapping("/{productId}/comment")
    fun postComment(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid comment")
        comment: String
    ): ResponseEntity<String> {
        val newProduct = productRestService.postComment(comment, productId)

        return ResponseEntity(newProduct, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    @PostMapping()
    fun addProduct(
        @RequestBody
        @NotNull(message = "Insert a valid product")
        product: ProductDTO
    ): ResponseEntity<Any> {
        val newProduct = productRestService.addProduct(product)

        return ResponseEntity(newProduct, HttpStatus.OK)
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    @PatchMapping("/{productId}")
    fun editProduct(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid comment")
        newProduct: ProductDTO
    ): ResponseEntity<Any> {
        val responseStatusCode = productRestService.editProduct(newProduct, productId)

        return ResponseEntity(responseStatusCode)
    }

    //TODO sistemare
    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    @PutMapping("/{productId}")
    fun uploadPicture(
        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestParam
        @NotNull(message = "Insert a valid picture")
        picture: MultipartFile
    ): ResponseEntity<String> {
        val response = productRestService.uploadPicture(picture, productId)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
