package it.polito.wa2project.wa2projectcatalogservice.dto

data class OrderResponseDTO(
    val orderId: Long?,
    val uuid: String,
    val exitStatus: Long
)