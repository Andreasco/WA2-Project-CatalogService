package it.polito.wa2project.wa2projectcatalogservice.repositories

import it.polito.wa2project.wa2projectcatalogservice.domain.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: CrudRepository<Customer, Long>
