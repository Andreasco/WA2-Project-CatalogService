package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.ProductDTO
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.collections.HashMap

@Service
class ProductRestService(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate: RestTemplate

    private val warehouseServiceURL = "http://localhost:8200/warehouseService/products" //TODO cambia la porta?

    init {
        restTemplate = restTemplateBuilder.build()
    }

    /* MARKETPLACE CONTROLLER *******************************************/

    fun getProductsByCategory(category: String?): ResponseEntity<String>{
        val url = "$warehouseServiceURL/?category=${category ?: ""}"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET ALL PRODUCTS: Warehouse service response $responseEntity")

        return responseEntity
    }

    fun getProductInfo(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCT INFO: Warehouse service response $responseEntity")

        return responseEntity
    }

    fun getProductPicture(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/picture"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCT PICTURE: Warehouse service response $responseEntity")

        return responseEntity
    }

    /* PRODUCT CONTROLLER ***********************************************/

    fun giveStars(stars: Int, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/stars"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        val map: MutableMap<String, Any> = HashMap()
        map["stars"] = stars

        //Build the request
        val entity = HttpEntity(map, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(url, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, entity)

        println("GIVE STARS: Warehouse service response $responseEntity")

        return responseEntity
    }

    fun postComment(comment: String, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/comment"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        val map: MutableMap<String, Any> = HashMap()
        map["comment"] = comment

        //Build the request
        val entity = HttpEntity(map, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(url, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, entity)

        println("POST COMMENT: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun addProduct(product: ProductDTO): ResponseEntity<String>{
        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(product, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(warehouseServiceURL, entity)

        println("ADD PRODUCT: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun deleteProduct(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId"

        //Send DELETE request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)

        println("DELETE PRODUCT: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun editProduct(newProduct: ProductDTO, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newProduct, headers)

        //Send PATCH request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PATCH, entity)

        println("EDIT PRODUCT: Response status code $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun uploadPicture(picture: MultipartFile, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/picture"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(picture, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(url, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, entity)

        println("UPLOAD PICTURE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getProductWarehouses(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/warehouses"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCT WAREHOUSES: Warehouse service response $responseEntity")

        return responseEntity
    }
}
