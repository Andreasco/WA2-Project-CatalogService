package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.ProductDTO
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
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

    fun getAllProducts(): String{
        val response: String = restTemplate.getForObject(warehouseServiceURL) //Dovrebbe contenere il JSON che mi manda warehouse

        println("GET ALL PRODUCTS: Warehouse service response $response")

        return response
    }

    fun getProductInfo(productId: Long): String{
        val url = "$warehouseServiceURL/$productId"

        val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        println("GET PRODUCT INFO: Warehouse service response $response")

        return response
    }

    /* PRODUCT CONTROLLER ***********************************************/

    fun giveStars(stars: Int, productId: Long): String{
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
        val response: String = restTemplate.postForObject(url, entity)

        println("GIVE STARS: Warehouse service response $response")

        return response //TODO controllare se ritorna il JSON se metto String come valore nel postForObject
    }

    fun postComment(comment: String, productId: Long): String{
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
        val response: String = restTemplate.postForObject(url, entity)

        println("POST COMMENT: Warehouse service response $response")

        return response
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun addProduct(product: ProductDTO): String{
        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(product, headers)

        //Send POST request
        val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        println("ADD PRODUCT: Warehouse service response $response")

        return response
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun deleteProduct(productId: Long): HttpStatus{
        val url = "$warehouseServiceURL/$productId"

        //Send DELETE request
        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)
        val responseStatusCode = response.statusCode

        println("DELETE PRODUCT: Warehouse service response $response")

        return responseStatusCode
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun editProduct(newProduct: ProductDTO, productId: Long): HttpStatus{
        val url = "$warehouseServiceURL/$productId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newProduct, headers)

        //Send PUT request
        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PUT, entity)
        val responseStatusCode = response.statusCode

        println("EDIT PRODUCT: Response status code $responseStatusCode")

        return responseStatusCode
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun uploadPicture(picture: MultipartFile, productId: Long): String{
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
        val response: String = restTemplate.postForObject(url, entity)

        println("UPLOAD PICTURE: Warehouse service response $response")

        return response
    }
}
