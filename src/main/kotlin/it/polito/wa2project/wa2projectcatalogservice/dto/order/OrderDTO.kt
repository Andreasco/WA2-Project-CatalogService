package it.polito.wa2project.wa2projectcatalogservice.dto.order

data class OrderDTO(
    var buyerId: Long?,

    var deliveryName: String?,
    var deliveryStreet: String?,
    var deliveryZip: String?,
    var deliveryCity: String?,
    var deliveryNumber: String?,

    var status: OrderStatus?,

    var orderProducts: Set<OrderProductDTO>?
)
