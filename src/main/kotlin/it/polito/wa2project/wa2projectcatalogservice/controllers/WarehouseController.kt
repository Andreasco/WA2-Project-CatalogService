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

    @PutMapping("/{warehouseId}")
    fun updateOrCreateWarehouse(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid warehouse")
        newWarehouse: WarehouseDTO
    ): ResponseEntity<String> {

        return warehouseRestService.updateOrCreateWarehouse(newWarehouse, warehouseId)
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

    @GetMapping("/{warehouseId}/products")
    fun getWarehouseProducts(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long
    ): ResponseEntity<String> {

        return warehouseRestService.getWarehouseProducts(warehouseId)
    }

    @GetMapping("/{warehouseId}/storages")
    fun getProductsStorages(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long
    ): ResponseEntity<String> {

        return warehouseRestService.getProductsStorages(warehouseId)
    }

    @PostMapping("/{warehouseId}/products/{productId}/load")
    fun loadProduct(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestParam
        @Positive(message = "Insert a valid load quantity")
        loadQuantity: Int
    ): ResponseEntity<String> {

        return warehouseRestService.loadProduct(warehouseId, productId, loadQuantity)
    }

    @PostMapping("/{warehouseId}/products/{productId}/unload")
    fun unloadProduct(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestParam
        @Positive(message = "Insert a valid unload quantity")
        unloadQuantity: Int
    ): ResponseEntity<String> {

        return warehouseRestService.unloadProduct(warehouseId, productId, unloadQuantity)
    }

    @GetMapping("/{warehouseId}/products/{productId}/isQuantityBelowAlarmLevel")
    fun checkProductQuantityIsBelowAlarmLevel(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long
    ): ResponseEntity<String>{

        return warehouseRestService.checkProductQuantityIsBelowAlarmLevel(warehouseId, productId)
    }

    @PostMapping("{warehouseId}/products/{productId}/alarmLevel")
    fun updateProductAlarmLevel(
        @PathVariable
        @Positive(message = "Insert a valid warehouseId")
        warehouseId: Long,

        @PathVariable
        @Positive(message = "Insert a valid productId")
        productId: Long,

        @RequestParam
        @Positive(message = "An alarmLevel is required")
        newLevel: Int,
    ): ResponseEntity<String>{

        return warehouseRestService.updateProductAlarmLevel(warehouseId, productId, newLevel)
    }
}
