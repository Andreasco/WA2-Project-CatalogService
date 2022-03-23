package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.dto.wallet.TransactionRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class WalletRestService(restTemplateBuilder: RestTemplateBuilder, val userRepository: UserRepository) {
    private val restTemplate: RestTemplate

    private val walletServiceURL = "http://localhost:8200/walletservice/wallets"

    init {
        restTemplate = restTemplateBuilder.build()
    }

    fun getWallet(walletId: Long): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val url = "$walletServiceURL/$walletId/$userId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET WALLET: Wallet service response $responseEntity")

        return responseEntity
    }

    fun addWallet(): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        //val map: MutableMap<String, Any> = HashMap()
        //map["userId"] = userId

        //Build the request
        val entity = HttpEntity(userId, headers) //Because walletService gets a simple Long

        //Send POST request
        //val response: String = restTemplate.postForObject(walletServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(walletServiceURL, entity)

        println("ADD WALLET: Wallet service response $responseEntity")

        return responseEntity
    }

    fun getTransactionsBetweenDate(walletId: Long, from: Long, to: Long): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val url = "$walletServiceURL/$walletId/transactions?from=$from&to=$to&buyerId=$userId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET TRANSACTIONS BETWEEN DATES: Wallet service response $responseEntity")

        return responseEntity
    }

    fun getTransaction(walletId: Long, transactionId: Long): ResponseEntity<String>{
        val url = "$walletServiceURL/$walletId/transactions/$transactionId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)

        println("GET TRANSACTION: Wallet service response $responseEntity")

        return responseEntity
    }

    fun rechargeWallet(rechargeDTO: TransactionRequestDTO): ResponseEntity<String>{
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        val userId = userRepository.findByUsername(usernameLogged)!!.getId()!!

        val url = "$walletServiceURL/transactions"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        //val map: MutableMap<String, Any> = HashMap()
        //map["userId"] = userId

        //Build the request
        rechargeDTO.reason = "RECHARGE"
        rechargeDTO.userID = userId

        val entity = HttpEntity(rechargeDTO, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(walletServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, entity)

        println("RECHARGE WALLET: Wallet service response $responseEntity")

        return responseEntity
    }

    fun refundWallet(orderRequestUuid: String): ResponseEntity<String>{
        val url = "$walletServiceURL/orderrequests/$orderRequestUuid"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val responseEntity: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.DELETE)

        println("REFUND WALLET: Wallet service response $responseEntity")

        return responseEntity
    }

    fun undoRefundUser(orderRequestUuid: String): ResponseEntity<String>{
        val url = "$walletServiceURL/orderrequests/$orderRequestUuid"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url)

        println("UNDO REFUND WALLET: Wallet service response $responseEntity")

        return responseEntity
    }
}
