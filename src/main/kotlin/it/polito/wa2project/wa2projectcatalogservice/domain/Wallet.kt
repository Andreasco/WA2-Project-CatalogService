package it.polito.wa2project.wa2projectcatalogservice.domain

import javax.persistence.*
import javax.validation.constraints.Positive

@Entity
class Wallet(
    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    var customer: Customer,

    @Positive(message = "Wallet money amount most be positive")
    var currentAmount: Double = 0.0,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceWallet", targetEntity = Transaction::class)
    var outgoingTransactions: MutableSet<Transaction> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationWallet", targetEntity = Transaction::class)
    var ingoingTransactions: MutableSet<Transaction> = mutableSetOf()
): EntityBase<Long>()
