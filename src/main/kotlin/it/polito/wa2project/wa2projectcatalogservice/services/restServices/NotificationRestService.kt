package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import java.util.*
import kotlin.collections.HashMap


@Service
class NotificationRestService(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate: RestTemplate

    private val notificationServiceURL = "http://localhost:8200/notificationService/notification"

    init {
        restTemplate = restTemplateBuilder.build()
    }

    fun sendEmail(email: String, subject: String, text: String){
        val url = "$notificationServiceURL/sendEmail"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        val map: MutableMap<String, Any> = HashMap()
        map["destinationAddress"] = email
        map["subject"] = subject
        map["text"] = text

        //Build the request
        val entity = HttpEntity(map, headers)

        //Send POST request
        val response: String = restTemplate.postForObject(url, entity)

        println("SEND MAIL: Notification service response $response")
    }

    fun getEmailVerificationToken(username: String): String{
        val url = "$notificationServiceURL/createToken"

        //Create headers
        val headers = HttpHeaders()

        //Set `content-type` header
        headers.contentType = MediaType.APPLICATION_JSON

        //Set `accept` header
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        //Create a map for post parameters
        val map: MutableMap<String, Any> = HashMap()
        map["username"] = username

        //Build the request
        val entity = HttpEntity(map, headers)

        //Send POST request
        val token: String = restTemplate.postForObject(url, entity)

        println("GET EMAIL NOTIFICATION TOKEN: Token: $token")

        return token
    }

    fun getUsernameFromEmailVerificationToken(token: String): String {
        val url = "$notificationServiceURL/validateToken?token=$token"

        val username: String = restTemplate.getForObject(url)

        println("GET USERNAME: Username: $token")

        return username
    }
}
