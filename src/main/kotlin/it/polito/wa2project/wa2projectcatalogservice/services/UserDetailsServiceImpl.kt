package it.polito.wa2project.wa2projectcatalogservice.services

import it.polito.wa2project.wa2projectcatalogservice.domain.Customer
import it.polito.wa2project.wa2projectcatalogservice.domain.Rolename
import it.polito.wa2project.wa2projectcatalogservice.domain.User
import it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserDetailsDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.auth.toUserDetailsDTO
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserDetailsServiceImpl(
    val userRepository: UserRepository,
    val restService: RestService,
    val passwordEncoder: PasswordEncoder
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetailsDTO {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Selected username is not present in the DB")

        return user.toUserDetailsDTO()
    }

    fun createUser(
        username: String,
        password: String,
        email: String,
        name: String,
        surname: String,
        address: String,
    ): UserDetailsDTO {
        val newCustomer = Customer(name, surname, address, email)
        val newUser = User(username, passwordEncoder.encode(password), email, newCustomer)
        newUser.addRolename(Rolename.CUSTOMER)

        val u = userRepository.saveAndFlush(newUser) // This will throw an SQLException if the email or the username are duplicate

        val emailVerificationToken = restService.getEmailVerificationToken(username)

        //Old email message
        /*val emailConfirmationMessage =
            """
                Hello $name and welcome to our e-commerce!
                
                Before you can continue, please click this link to verify your email address:
                http://localhost:8080/auth/registrationConfirm?token=${emailVerificationToken.getToken()}
                
                Please note that this link will expire in 30 minutes.
            """.trimIndent()*/

        val emailConfirmationMessage =
        """
            Hi,
            Welcome to WA2-Labs. Before you can login you will need to activate your account to complete the registration process.
            
            Please take a note of the username you chose as you will need it to login:
            $username
            
            Please follow the below link to complete the registration process.
            You will not be able to login until you do it.
            
            http://localhost:8080/auth/registrationConfirm?token=${emailVerificationToken}
            
            Please note that this link will expire in 30 minutes.
            
            Thanks,
            The GA-team
            
            If you did not request to sign up, please ignore this e-mail.
        """.trimIndent()

        restService.sendEmail(email, "Confirm your email", emailConfirmationMessage)

        return u.toUserDetailsDTO()
    }

    fun addRole(username: String, rolename: Rolename): UserDetailsDTO {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Selected username is not present in the DB")

        user.addRolename(rolename)

        return userRepository.save(user).toUserDetailsDTO()
    }

    fun removeRole(username: String, rolename: Rolename): UserDetailsDTO {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Selected username is not present in the DB")

        user.removeRolename(rolename)

        return userRepository.save(user).toUserDetailsDTO()
    }

    fun enableUserWithToken(emailToken: String): UserDetailsDTO {
        //TODO implement notification service using external service
        val username = "prova" //notificationService.getUsernameFromEmailVerificationToken(emailToken)
        return setIsEnabled(username, true)
    }

    @PreAuthorize("hasRole('ADMIN')") // This works both with "ROLE_ADMIN" and "ADMIN"
    fun enableUser(username: String): UserDetailsDTO {
        return setIsEnabled(username, true)
    }

    fun disableUser(username: String): UserDetailsDTO {
        return setIsEnabled(username, false)
    }

    private fun setIsEnabled(username: String, isEnabled: Boolean): UserDetailsDTO {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Selected username is not present in the DB")

        user.isEnabled = isEnabled

        return userRepository.save(user).toUserDetailsDTO()
    }

    fun getUserById(id: Long): UserDetailsDTO{
        val user = userRepository.findById(id)

        if (user.isEmpty)
            throw UsernameNotFoundException("Selected username is not present in the DB")

        return user.get().toUserDetailsDTO()
    }
}
