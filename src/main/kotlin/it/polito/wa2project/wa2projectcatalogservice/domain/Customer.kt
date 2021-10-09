package it.polito.wa2project.wa2projectcatalogservice.domain

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Customer(
    var name: String,
    var surname: String,
    var deliveryAddress: String,

    @Column(unique = true)
    @NotNull
    var email: String,

    @OneToOne(mappedBy = "customer")
    var user: User? = null,

    @OneToMany(mappedBy = "customer", targetEntity = Wallet::class)
    var wallets: MutableSet<Wallet> = mutableSetOf()
): EntityBase<Long>()
