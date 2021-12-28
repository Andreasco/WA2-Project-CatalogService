package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.*
import java.util.*

@Service
class WalletRestService(restTemplateBuilder: RestTemplateBuilder, userRepository: UserRepository) {
    private val restTemplate: RestTemplate

    private val walletServiceURL = "http://localhost:8200/walletService/wallets" //TODO cambia la porta?
    private val userId: Long

    init {
        restTemplate = restTemplateBuilder.build()
        val usernameLogged = SecurityContextHolder.getContext().authentication.principal as String
        userId = userRepository.findByUsername(usernameLogged)!!.getId()!!
    }

    //TODO aggiungo anche lo userId per sicurezza?
    fun getWallet(walletId: Long): String{
        val url = "$walletServiceURL/$walletId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET WALLET: Wallet service response $response")

        return response
    }

    fun addWallet(): String{
        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        val map: MutableMap<String, Any> = HashMap()
        map["userId"] = userId

        //Build the request
        val entity = HttpEntity(map, headers)

        //Send POST request
        //val response: String = restTemplate.postForObject(walletServiceURL, entity)

        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(walletServiceURL, entity)
        val response = responseEntity.body!!

        println("ADD WALLET: Wallet service response $response")

        return response
    }

    //TODO aggiungo anche l'userId per essere sicuro di chi richiede le transazioni?
    fun getTransactionsBetweenDate(walletId: Long, from: Long, to: Long): String{
        val url = "$walletServiceURL/$walletId/transactions?from=$from&to=$to"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET TRANSACTIONS BETWEEN DATES: Wallet service response $response")

        return response
    }

    fun getTransaction(walletId: Long, transactionId: Long): String{
        val url = "$walletServiceURL/$walletId/transactions/$transactionId"

        //val response: String = restTemplate.getForObject(url) //Dovrebbe contenere il JSON che mi manda wallet service

        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url)
        val response = responseEntity.body!!

        println("GET TRANSACTION: Wallet service response $response")

        return response
    }
}