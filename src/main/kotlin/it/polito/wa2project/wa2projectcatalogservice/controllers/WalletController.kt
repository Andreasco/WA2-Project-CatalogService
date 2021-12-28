package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.services.restServices.WalletRestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/wallets")
@Validated
class WalletController(val walletRestService: WalletRestService) {

    @GetMapping("/{walletId}")
    fun getWallet(
        @PathVariable
        @Positive(message = "Insert a valid walletId")
        walletId: Long
    ): ResponseEntity<String> {
        val response = walletRestService.getWallet(walletId)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/{walletId}")
    fun addWallet(
        @PathVariable
        @Positive(message = "Insert a valid walletId")
        walletId: Long
    ): ResponseEntity<String> {
        val response = walletRestService.addWallet()

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/{walletId}/transactions")
    fun getTransactionsBetweenDate(
        @PathVariable
        @Positive(message = "Insert a valid walletId")
        walletId: Long,

        @RequestParam
        @NotNull(message = "'from' timestamp is required")
        @Positive(message = "Insert a valid 'from' timestamp")
        from: Long? = null,

        @RequestParam
        @NotNull(message = "'to' timestamp is required")
        @Positive(message = "Insert a valid 'to' timestamp")
        to: Long? = null
    ): ResponseEntity<String>{
        val setOfTransaction = walletRestService.getTransactionsBetweenDate(walletId, from!!, to!!)

        return ResponseEntity(setOfTransaction, HttpStatus.OK)
    }

    @GetMapping("{walletId}/transactions/{transactionId}")
    fun getTransaction(
        @PathVariable
        @Positive(message = "Insert a valid walletId")
        walletId: Long,

        @PathVariable
        @Positive(message = "Insert a valid transactionId")
        transactionId: Long,
    ): ResponseEntity<String>{
        val response = walletRestService.getTransaction(walletId, transactionId)

        return ResponseEntity(response, HttpStatus.OK)
    }
}
