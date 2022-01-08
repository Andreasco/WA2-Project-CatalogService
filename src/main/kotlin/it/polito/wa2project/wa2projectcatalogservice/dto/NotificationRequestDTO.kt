package it.polito.wa2project.wa2projectcatalogservice.dto

data class NotificationRequestDTO(
    val messageObject: String,
    val message: String,
    val userId: Long? //If null send email to all admins
)
