package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.domain.Rolename
import it.polito.wa2project.wa2projectcatalogservice.dto.LoginRequestBodyDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.UserDetailsDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.UserRegistrationBodyDTO
import it.polito.wa2project.wa2projectcatalogservice.security.JwtUtils
import it.polito.wa2project.wa2projectcatalogservice.services.UserDetailsServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/auth")
@Validated
class AuthenticationController(val userDetailsService: UserDetailsServiceImpl,
                               val authenticationManager: AuthenticationManager,
                               val jwtUtils: JwtUtils
) {
    /**
     * This endpoint is used by user to register themselves.
     */
    @PostMapping("/register")
    fun addUser(
        @RequestBody
        @Valid
        bodyDTO: UserRegistrationBodyDTO
    ): ResponseEntity<UserDetailsDTO> {
        val newUserDTO = userDetailsService.createUser(bodyDTO.username,
                                      bodyDTO.password,
                                      bodyDTO.email,
                                      bodyDTO.name,
                                      bodyDTO.surname,
                                      bodyDTO.address)

        return ResponseEntity(newUserDTO, HttpStatus.CREATED)
    }

    @GetMapping("/registrationConfirm")
    fun confirmRegistration(
        @RequestParam
        @NotNull(message = "'token' param is required")
        token: String? = null
    ): ResponseEntity<UserDetailsDTO> {
        val userDTO = userDetailsService.enableUserWithToken(token!!)

        return ResponseEntity(userDTO, HttpStatus.OK)
    }

    @PostMapping("/signin")
    fun login(
        @RequestBody
        @Valid
        bodyDTO: LoginRequestBodyDTO
    ): ResponseEntity<String> {
        val authentication: Authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(bodyDTO.username, bodyDTO.password))

        val token: String = jwtUtils.generateJwtToken(authentication)
        return ResponseEntity("JWT Token: $token", HttpStatus.OK)
    }

    /**
     * This endpoint is for admins only.
     *
     * It may be used to authorize a user manually.
     */
    @PutMapping("/enableUser")
    fun enableUserAdmin(
        @RequestBody
        @NotNull(message = "Username is required")
        username: String? = null,
    ): ResponseEntity<UserDetailsDTO> {
        val enabledUserDTO = userDetailsService.enableUser(username!!)

        return ResponseEntity(enabledUserDTO, HttpStatus.OK)
    }
}
