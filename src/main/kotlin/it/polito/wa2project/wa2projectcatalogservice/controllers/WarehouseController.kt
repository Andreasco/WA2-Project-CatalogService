package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.WarehouseDTO
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.WarehouseRestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/warehouses")
@Validated
class WarehouseController(val warehouseRestService: WarehouseRestService) {
    @GetMapping
    fun getWarehouses(): ResponseEntity<String> {
        val response = warehouseRestService.getWarehouses()

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{warehouseId}")
    fun getWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long
    ): ResponseEntity<String> {
        val response = warehouseRestService.getWarehouse(warehouseId)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping
    fun addWarehouse(
        @RequestBody
        @NotNull(message = "Insert a valid warehouse")
        warehouse: WarehouseDTO
    ): ResponseEntity<Any> {
        val newWarehouse = warehouseRestService.addWarehouse(warehouse)

        return ResponseEntity(newWarehouse, HttpStatus.OK)
    }

    @PatchMapping("/{warehouseId}")
    fun editWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid warehouse")
        newWarehouse: WarehouseDTO
    ): ResponseEntity<Any> {
        val responseStatusCode = warehouseRestService.editWarehouse(newWarehouse, warehouseId)

        return ResponseEntity(responseStatusCode)
    }

    @DeleteMapping("/{warehouseId}")
    fun deleteWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,
    ): ResponseEntity<Any> {
        val responseStatusCode = warehouseRestService.deleteWarehouse(warehouseId)

        return ResponseEntity(responseStatusCode)
    }
}
