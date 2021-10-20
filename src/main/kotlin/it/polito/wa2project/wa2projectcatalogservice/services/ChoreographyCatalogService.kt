package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.dto.WarehouseRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.WarehouseResponseDTO
import org.springframework.kafka.annotation.KafkaListener

class ChoreographyCatalogService {

    @KafkaListener(topics = arrayOf("warehouseLoadResponse"))
        //, groupId = "foo")
    fun loadItemsResponse( response: String) {
        println(response)
    }
}