package it.polito.wa2project.wa2projectcatalogservice.dto.wallet

import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class TransactionRequestDTO (
    @field:Positive(message = "Money amount must be positive")
    @field:NotNull(message = "Money amount is required")
    var moneyAmount: Double? = null,

    @field:Positive(message = "Insert a valid destination wallet id")
    @field:NotNull(message = "Destination wallet id is required")
    var destinationWalletId: Long? = null,

    var reason: String?,

    var userID: Long?
)
