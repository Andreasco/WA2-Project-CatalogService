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

    private val warehouseServiceURL = "http://localhost:8200/warehouseService/warehouses" //TODO cambia la porta?

    init {
        restTemplate = restTemplateBuilder.build()
    }

    fun getWarehouses(): ResponseEntity<String>{
        //val response: String = restTemplate.getForObject(warehouseServiceURL) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(warehouseServiceURL)

        println("GET WAREHOUSES: Warehouse service response $responseEntity")

        return responseEntity
    }

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
}
