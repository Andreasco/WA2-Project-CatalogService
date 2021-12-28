package it.polito.wa2project.wa2projectcatalogservice.repositories

import it.polito.wa2project.wa2projectcatalogservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Query("SELECT u.email FROM User u WHERE u.roles = 'ADMIN'") //TODO controllare se in roles ADMIN è scritto così
    fun findAdminsEmails(): Set<String>
}
