package it.polito.wa2project.wa2projectcatalogservice.dto.product

import java.sql.Timestamp

data class CommentDTO (
    var id: Long?,
    var title: String,
    var body: String?,
    var stars: Int,
    var creation_date: Timestamp = Timestamp(System.currentTimeMillis()),
    var productId: Long?
)
