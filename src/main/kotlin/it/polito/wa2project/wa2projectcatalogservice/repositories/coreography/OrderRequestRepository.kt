package it.polito.wa2project.wa2projectcatalogservice.repositories.coreography

import it.polito.wa2project.wa2projectcatalogservice.domain.coreography.OrderRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRequestRepository: CrudRepository<OrderRequest, Long>{
    fun findByUuid(uuid: String): OrderRequest?
}
