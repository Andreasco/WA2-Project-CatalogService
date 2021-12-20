package it.polito.wa2project.wa2projectcatalogservice.dto.warehouse

import java.sql.Timestamp

data class ProductDTO (var name: String,
                       var description: String?,
                       var picture_URL: String?,
                       var category: String,
                       var price: Double,
                       var average_rating: Float = Float.MIN_VALUE,
                       var creation_date: Timestamp = Timestamp(System.currentTimeMillis()),
                       var comments: Set<Long?> = setOf(),
                       var storageWarehouses: Set<Long?> = setOf()
)
