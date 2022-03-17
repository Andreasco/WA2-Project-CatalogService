package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.dto.wallet.RechargeWalletDTO
import it.polito.wa2project.wa2projectcatalogservice.services.restServices.WalletRestService
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

        return walletRestService.getWallet(walletId)
    }

    @PostMapping
    fun addWallet(): ResponseEntity<String> {
        return walletRestService.addWallet()
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

        return walletRestService.getTransactionsBetweenDate(walletId, from!!, to!!)
    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    fun getTransaction(
        @PathVariable
        @Positive(message = "Insert a valid walletId")
        walletId: Long,

        @PathVariable
        @Positive(message = "Insert a valid transactionId")
        transactionId: Long,
    ): ResponseEntity<String>{

        return walletRestService.getTransaction(walletId, transactionId)
    }

    @PostMapping("/transactions")
    fun rechargeWallet(
        @RequestBody
        @Positive(message = "Insert a valid transactionId")
        rechargeDTO: RechargeWalletDTO,
    ): ResponseEntity<String>{

        return walletRestService.rechargeWallet(rechargeDTO)
    }
}
