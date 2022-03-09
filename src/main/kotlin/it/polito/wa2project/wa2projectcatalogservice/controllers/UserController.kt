package it.polito.wa2project.wa2projectcatalogservice.controllers

import it.polito.wa2project.wa2projectcatalogservice.domain.Rolename
import it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserDetailsDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserPublicDTO
import it.polito.wa2project.wa2projectcatalogservice.services.UserDetailsServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/user")
@Validated
class UserController(val userDetailsServiceImpl: UserDetailsServiceImpl) {

    @GetMapping
    fun getLoggedUserDetails(): ResponseEntity<UserPublicDTO> {
        val userDTO = userDetailsServiceImpl.getLoggedUser()

        return ResponseEntity(userDTO, HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    fun getUserDetails(
        @PathVariable
        @Positive(message = "Insert a valid userId")
        userId: Long
    ): ResponseEntity<UserDetailsDTO> {
        val userDTO = userDetailsServiceImpl.getUserByIdController(userId)

        return ResponseEntity(userDTO, HttpStatus.OK)
    }

    @PatchMapping
    fun updateUser(
        @RequestBody
        @NotNull(message = "Insert a valid user")
        newUser: UserPublicDTO
    ): ResponseEntity<UserPublicDTO> {
        val updatedUser = userDetailsServiceImpl.updateUser(newUser)

        //Return the user that I've just updated
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @PostMapping("/{userId}/addRole")
    fun addRole(
        @PathVariable
        @Positive(message = "Insert a valid userId")
        userId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid rolename")
        newRole: Rolename
    ): ResponseEntity<UserPublicDTO> {
        val updatedUser = userDetailsServiceImpl.addRole(userId, newRole)

        //Return the user that I've just updated
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @PostMapping("/{userId}/removeRole")
    fun removeRole(
        @PathVariable
        @Positive(message = "Insert a valid userId")
        userId: Long,

        @RequestBody
        @NotNull(message = "Insert a valid rolename")
        role: Rolename
    ): ResponseEntity<UserPublicDTO> {
        val updatedUser = userDetailsServiceImpl.removeRole(userId, role)

        //Return the user that I've just updated
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }
}
