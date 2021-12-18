package it.polito.wa2project.wa2projectcatalogservice.domain.coreography

import it.polito.wa2project.wa2projectcatalogservice.domain.Customer
import it.polito.wa2project.wa2projectcatalogservice.domain.EntityBase
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderProduct (
    @ManyToOne
    @JoinColumn(name = "orderRequest_id", referencedColumnName = "id")
    var orderRequest: OrderRequest?,

    var purchasedProductId: Long,             // productId of purchased product
    var quantity: BigInteger,
    var purchasedProductPrice: Double?,       // Product(productId)'s price
    var warehouseId: Long?
):EntityBase<Long>()