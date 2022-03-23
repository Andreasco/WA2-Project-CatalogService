package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.WarehouseDTO
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class WarehouseRestService(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate: RestTemplate

    private val warehouseServiceURL = "http://localhost:8200/warehouseservice/warehouses"

    init {
        restTemplate = restTemplateBuilder.build()
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getWarehouses(): ResponseEntity<String>{
        //val response: String = restTemplate.getForObject(warehouseServiceURL) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(warehouseServiceURL)

        println("GET WAREHOUSES: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getWarehouse(warehouseId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun addWarehouse(warehouseDTO: WarehouseDTO): ResponseEntity<String>{
        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(warehouseDTO, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(warehouseServiceURL, entity)

        println("ADD WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun updateOrCreateWarehouse(newWarehouseDTO: WarehouseDTO, warehouseId: Long): ResponseEntity<String> {
        val url = "$warehouseServiceURL/$warehouseId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newWarehouseDTO, headers)

        //Send PUT request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PUT, entity)

        println("UPDATE OR CREATE WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun editWarehouse(newWarehouseDTO: WarehouseDTO, warehouseId: Long): ResponseEntity<String> {
        val url = "$warehouseServiceURL/$warehouseId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newWarehouseDTO, headers)

        //Send PATCH request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PATCH, entity)

        println("EDIT WAREHOUSE: Response status code $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun deleteWarehouse(warehouseId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId"

        //Send DELETE request
        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)

        println("DELETE WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getWarehouseProducts(warehouseId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/products"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET WAREHOUSE PRODUCTS: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun getProductsStorages(warehouseId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/storages"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET PRODUCTS STORAGES: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun loadProduct(warehouseId: Long, productId: Long, loadQuantity: Int): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/products/$productId/load?quantity=$loadQuantity"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        //val entity = HttpEntity(loadQuantity, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url)

        println("ADD WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun unloadProduct(warehouseId: Long, productId: Long, unloadQuantity: Int): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/products/$productId/unload?quantity=$unloadQuantity"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        //val entity = HttpEntity(unloadQuantity, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url)

        println("ADD WAREHOUSE: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun checkProductQuantityIsBelowAlarmLevel(warehouseId: Long, productId: Long): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/products/$productId/isQuantityBelowAlarmLevel"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("CHECK PRODUCT QUANTITY LEVEL: Warehouse service response $responseEntity")

        return responseEntity
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun updateProductAlarmLevel(warehouseId: Long, productId: Long, newLevel: Int): ResponseEntity<String>{
        val url = "$warehouseServiceURL/$warehouseId/products/$productId/alarmLevel?newLevel=$newLevel"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        //val entity = HttpEntity(alarmLevel, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url)

        println("UPDATE PRODUCT ALARM LEVEL: Warehouse service response $responseEntity")

        return responseEntity
    }

    fun reloadProduct(orderRequestUuid: String): ResponseEntity<String>{
        val url = "$warehouseServiceURL/orderrequests/$orderRequestUuid"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)

        println("LOAD PRODUCT ORDER DELETION: Warehouse service response $responseEntity")

        return responseEntity
    }

    fun undoReloadProduct(orderRequestUuid: String): ResponseEntity<String>{
        val url = "$warehouseServiceURL/orderrequest/$orderRequestUuid"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url)

        println("UNLOAD PRODUCT ORDER DELETION: Warehouse service response $responseEntity")

        return responseEntity
    }
}
