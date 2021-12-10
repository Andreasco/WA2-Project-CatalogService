package it.polito.wa2project.wa2projectcatalogservice.dto.order

import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderProduct
import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderRequest
import java.math.BigInteger

data class OrderRequestDTO(
    var uuid: String,
    var orderId: Long?,
    var buyerId: Long?,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus?,

    var orderProducts: Set<OrderProductDTO>,
    var totalPrice: Double?,

    var destinationWalletId: Long?,
    var sourceWalletId: Long,

    var transactionReason: String?,
    var reasonDetail: Long?
)

data class OrderProductDTO(
    var purchasedProductId: Long,             // productId of purchased product
    var quantity: BigInteger,
    var purchasedProductPrice: Double?,       // Product(productId)'s price
    var warehouseId: Long?                    // warehouseId products are picked from
)

fun OrderRequest.toOrderRequestDTO() = OrderRequestDTO(
    uuid,
    orderId,
    buyerId,
    deliveryName,
    deliveryStreet,
    deliveryZip,
    deliveryCity,
    deliveryNumber,
    status,
    orderProducts.map{ it.toOrderProductDTO() }.toSet(),
    totalPrice,
    destinationWalletId,
    sourceWalletId,
    transactionReason,
    reasonDetail,
)

fun OrderProduct.toOrderProductDTO() = OrderProductDTO(
    purchasedProductId,
    quantity,
    purchasedProductPrice,
    warehouseId
)
