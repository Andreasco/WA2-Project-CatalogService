package it.polito.wa2project.wa2projectcatalogservice.services.restServices

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OrderRestService(restTemplateBuilder: RestTemplateBuilder) {
    private val restTemplate: RestTemplate

    private val warehouseServiceURL = "http://localhost:8200/orderService/orders" //TODO cambia la porta?

    init {
        restTemplate = restTemplateBuilder.build()
    }


}
