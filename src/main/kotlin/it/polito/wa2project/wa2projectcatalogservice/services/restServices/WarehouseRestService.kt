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

    fun getWarehouses(): String{
        //val response: String = restTemplate.getForObject(warehouseServiceURL) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(warehouseServiceURL)
        val response = responseEntity.body!!

        println("GET WAREHOUSES: Warehouse service response $response")

        return response
    }

    fun getWarehouse(warehouseId: Long): String{
        val url = "$warehouseServiceURL/$warehouseId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET WAREHOUSE: Warehouse service response $response")

        return response
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun addWarehouse(warehouse: WarehouseDTO): String{
        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(warehouse, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(warehouseServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(warehouseServiceURL, entity)
        val response = responseEntity.body!!

        println("ADD WAREHOUSE: Warehouse service response $response")

        return response
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun editWarehouse(newWarehouse: WarehouseDTO, warehouseId: Long): HttpStatus {
        val url = "$warehouseServiceURL/$warehouseId"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Build the request
        val entity = HttpEntity(newWarehouse, headers)

        //Send PATCH request
        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.PATCH, entity)
        val responseStatusCode = response.statusCode

        println("EDIT WAREHOUSE: Response status code $responseStatusCode")

        return responseStatusCode
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun deleteWarehouse(warehouseId: Long): HttpStatus{
        val url = "$warehouseServiceURL/$warehouseId"

        //Send DELETE request
        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)
        val responseStatusCode = response.statusCode

        println("DELETE WAREHOUSE: Warehouse service response $response")

        return responseStatusCode
    }
}
