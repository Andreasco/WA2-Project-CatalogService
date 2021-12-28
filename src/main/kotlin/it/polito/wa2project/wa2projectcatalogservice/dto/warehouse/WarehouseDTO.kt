package it.polito.wa2project.wa2projectcatalogservice.dto.warehouse

data class WarehouseDTO (
    var name: String?,
    var address: String?,
    var storageProducts: Set<Long?> = setOf()
)
