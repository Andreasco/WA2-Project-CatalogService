package it.polito.wa2project.wa2projectcatalogservice.repositories

import it.polito.wa2project.wa2projectcatalogservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
