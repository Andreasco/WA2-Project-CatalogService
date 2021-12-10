package it.polito.wa2project.wa2projectcatalogservice.dto.auth

import it.polito.wa2project.wa2projectcatalogservice.validators.PasswordValueMatch
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@PasswordValueMatch(
    password = "password",
    confirmPassword = "confirmPassword",
    message = "Password values don't match"
)
data class UserRegistrationBodyDTO(
    //User entity fields
    @field:NotNull(message = "Username is required")
    @field:Length(min = 4, max = 20, message = "Username must have at least 4 characters and max 20 characters")
    @field:Pattern(regexp = "(\\w|\\d|\\!|\\#|\\-)*\\S", message = "Username can only contain letters, digits or !,# or -")
    var username: String,

    @field:NotNull(message = "Password is required")
    @field:Length(min = 4, max = 20, message = "Password must have at least 4 characters and max 20 characters")
    var password: String,

    @field:NotNull(message = "Password confirmation is required")
    var confirmPassword: String,

    // Customer entity fields (the email is in both entities)
    @field:NotNull(message = "Email is required")
    @field:Email(message = "Provide a valid email address")
    var email: String,

    @field:NotNull(message = "Name is required")
    var name: String,

    @field:NotNull(message = "Surname is required")
    var surname: String,

    @field:NotNull(message = "Address is required")
    var address: String
)
