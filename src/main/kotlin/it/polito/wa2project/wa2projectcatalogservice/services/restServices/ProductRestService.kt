package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.dto.product.CommentDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.ProductDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import it.polito.wa2project.wa2projectcatalogservice.repositories.coreography.OrderRequestRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class ProductRestService(
    restTemplateBuilder: RestTemplateBuilder,
    val orderRequestRepository: OrderRequestRepository,
    val userRepository: UserRepository
) {
    private val restTemplate: RestTemplate

    private val warehouseServiceURL = "http://localhost:8200/warehouseservice/products"

    init {
        restTemplate = restTemplateBuilder.build()
    }

    /* MARKETPLACE CONTROLLER *******************************************/

    fun getProductsByCategory(category: String?): ResponseEntity<String>{
        val url = if (category == null)
            warehouseServiceURL
        else
            "$warehouseServiceURL?category=$category"

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

    fun getProductComments(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/comments"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCT COMMENTS: Warehouse service response $responseEntity")

        return responseEntity
    }

    /* PRODUCT CONTROLLER ***********************************************/

    fun postComment(comment: CommentDTO, productId: Long): ResponseEntity<String>{
        if (!productBoughtByLoggedUser(productId))
            return ResponseEntity("You cannot comment this product because you haven't bought it", HttpStatus.FORBIDDEN)

        val url = "$warehouseServiceURL/$productId/comments"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(comment, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(url, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, entity)

        println("POST COMMENT: Warehouse service response $responseEntity")

        return responseEntity
    }

    private fun productBoughtByLoggedUser(productId: Long): Boolean{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val orderRequestsList = orderRequestRepository.findByBuyerId(userId)

        orderRequestsList.forEach { request ->
            request.orderProducts.forEach { product ->
                if (product.purchasedProductId == productId)
                    return true
            }
        }

        return false
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
    fun updateOrCreateProduct(productId: Long, product: ProductDTO): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(product, headers)

        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PUT, entity)

        println("UPDATE OR CREATE PRODUCT: Warehouse service response $responseEntity")

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
    fun uploadProductPicture(pictureUrl: String, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/picture"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(pictureUrl, headers)

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

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getProductStorages(productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$productId/storages"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda warehouse

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCT STORAGES: Warehouse service response $responseEntity")

        return responseEntity
    }
}
