package it.polito.wa2project.wa2projectcatalogservice.dto.auth

import it.polito.wa2project.wa2projectcatalogservice.domain.User

data class UserPublicDTO(
    var username: String?,
    var customerId: Long,
    var email: String?,
    var isEnabled: Boolean?,
    var roles: String?
)

fun User.toUserPublicDTO() = UserPublicDTO(
    username,
    customer.getId()!!,
    email,
    isEnabled,
    roles
)
