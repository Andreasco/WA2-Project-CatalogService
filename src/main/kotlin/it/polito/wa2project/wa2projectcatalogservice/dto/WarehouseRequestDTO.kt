package it.polito.wa2project.wa2projectcatalogservice.dto

import java.math.BigInteger

data class WarehouseRequestDTO(
    val warehouseId: Long,
    val productId: Long,
    val quantity: BigInteger
    )