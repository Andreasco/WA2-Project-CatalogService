package it.polito.wa2project.wa2projectcatalogservice.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/user")
@Validated
class UserController {

    /*@GetMapping("/{userId}")
    fun getUser(
        @PathVariable
        @Positive(message = "Insert a valid userId")
        userId: Long
    ): ResponseEntity<UserDTO> {

        return ResponseEntity(userDTO, HttpStatus.OK)
    }

    //TODO va bene il link?
    @PutMapping("/update/{userId}")
    fun updateUser(
        @PathVariable
        @Positive(message = "Insert a valid userId")
        userId: Long
    ): ResponseEntity<UserDTO> {

        //Return the user that I've just updated
        return ResponseEntity(userDTO, HttpStatus.OK)
    }*/
}
