package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.warehouse.WarehouseDTO
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.WarehouseRestService
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

        return warehouseRestService.getWarehouses()
    }

    @GetMapping("/{warehouseId}")
    fun getWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long
    ): ResponseEntity<String> {

        return warehouseRestService.getWarehouse(warehouseId)
    }

    @PostMapping
    fun addWarehouse(
        @RequestBody
        @NotNull(message = "Insert a valid warehouse")
        warehouse: WarehouseDTO
    ): ResponseEntity<String> {

        return warehouseRestService.addWarehouse(warehouse)
    }

    @PatchMapping("/{warehouseId}")
    fun editWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid warehouse")
        newWarehouse: WarehouseDTO
    ): ResponseEntity<String> {

        return warehouseRestService.editWarehouse(newWarehouse, warehouseId)
    }

    @DeleteMapping("/{warehouseId}")
    fun deleteWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,
    ): ResponseEntity<String> {

        return warehouseRestService.deleteWarehouse(warehouseId)
    }
}
