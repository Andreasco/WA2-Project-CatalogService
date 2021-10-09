package it.polito.wa2project.wa2projectcatalogservice.domain

import java.sql.Timestamp
import javax.persistence.*

@Entity
class Transaction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceWalletId", referencedColumnName = "id")
    var sourceWallet: Wallet,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinationWalletId", referencedColumnName = "id")
    var destinationWallet: Wallet,

    var timestamp: Timestamp,
    var moneyAmount: Double
): EntityBase<Long>()
