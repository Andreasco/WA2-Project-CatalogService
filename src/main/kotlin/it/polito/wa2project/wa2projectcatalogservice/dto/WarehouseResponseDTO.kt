package it.polito.wa2project.wa2projectcatalogservice.dto

import java.math.BigInteger

data class WarehouseResponseDTO(
    var warehouseId: Long,
    var productId: Long,
    var quantity: BigInteger,
    var alarmLevel: BigInteger
)