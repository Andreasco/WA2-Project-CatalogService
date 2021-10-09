package it.polito.wa2project.wa2projectcatalogservice.dto

import javax.validation.constraints.NotNull

class LoginRequestBodyDTO(
    @field:NotNull(message = "Username is required")
    var username: String? = null,

    @field:NotNull(message = "Password field is required")
    var password: String? = null
)
