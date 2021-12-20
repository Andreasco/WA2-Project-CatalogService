package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.services.ProductRestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/marketplace")
class MarketplaceController(val productRestService: ProductRestService) {

    //TODO controllare se mettere ProductDTO al posto di Any (any potrebbe servire nel caso fosse possibile
    //semplicemente inoltrare la risposta del warehouse)
    @GetMapping("/products")
    fun getAllProducts(): ResponseEntity<String>{ //Dovrebbe contenere il JSON che mi manda warehouse
        val productsList = productRestService.getAllProducts()

        return ResponseEntity(productsList, HttpStatus.OK)
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
        val product = productRestService.getProductInfo(productId)

        return ResponseEntity(product, HttpStatus.OK)
    }
}
