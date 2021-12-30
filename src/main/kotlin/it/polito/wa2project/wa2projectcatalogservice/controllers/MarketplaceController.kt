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

    //TODO facciamo un metodo che ritorna tutte le informazioni di tutti i prodotti oppure un metodo che ritorna
    //solo alcune informazioni per ogni prodotto tipo id, nome, prezzo e disponibilità e un altro metodo che
    //dato un id ritorna tutte le altre informazioni?

    //Eventualmente questo sarebbe il metodo per avere tutte le altre informazioni

    //TODO controllare se mettere ProductDTO al posto di Any (any potrebbe servire nel caso fosse possibile
    //semplicemente inoltrare la risposta del warehouse)
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
}
