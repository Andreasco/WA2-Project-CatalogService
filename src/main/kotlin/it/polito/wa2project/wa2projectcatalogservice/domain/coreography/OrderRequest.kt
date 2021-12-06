package it.polito.wa2project.wa2projectcatalogservice.domain.coreography

import it.polito.wa2project.wa2projectcatalogservice.domain.EntityBase
import it.polito.wa2project.wa2projectcatalogservice.domain.Wallet
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderProductDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderStatus
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class OrderRequest (
    private var uuid: String = UUID.randomUUID().toString()

    var orderId: Long?,
    var buyerId: Long?,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus?,

    var totalPrice: Double?,

    var destinationWalletId: Long?,
    var sourceWalletId: Long,

    var transactionReason: String?,
    var reasonDetail: Long?,

    @OneToMany(mappedBy = "orderRequest", targetEntity = OrderProduct::class)
    var orderProducts: MutableSet<OrderProduct> = mutableSetOf(),
    ): EntityBase<Long>() {

    fun addOrderProduct( orderProduct: OrderProduct ){
        orderProducts.add(orderProduct)
        orderProduct.orderRequest = this
    }
}