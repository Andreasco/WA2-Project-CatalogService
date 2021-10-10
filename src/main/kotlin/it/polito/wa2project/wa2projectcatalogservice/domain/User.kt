package it.polito.wa2project.wa2projectcatalogservice.domain

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

@Table(indexes = [Index(name = "username_index", columnList = "username", unique = true)])
@Entity
class User(
    @NotNull
    @Column(unique = true)
    var username: String,

    var password: String,

    @Column(unique = true)
    @Email
    var email: String,

    // Apply all cascading effects to the related entity.
    // That is, whenever we update/delete a User entity,
    // update/delete the corresponding Customer as well.
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    var customer: Customer,

    var isEnabled: Boolean = false,

    var roles: String = ""
): EntityBase<Long>(){

    fun getRolenames(): Set<Rolename> {
        val result = roles.split(",")

        return if(result[0] != "")
            result.map{ Rolename.valueOf(it) }.toSet()
        else
            emptySet()
    }

    fun addRolename(rolename: Rolename) {
        val actualRoles = getRolenames()
        if(!actualRoles.contains(rolename)){
            if(roles  != "")
                roles += ","

            roles += rolename.name
        }
    }

    fun removeRolename(rolename: Rolename) {
        val resultRoles = getRolenames() - rolename

        var result = ""
        resultRoles.forEachIndexed{ index, el ->
            result += el

            if(index != resultRoles.size - 1)
                result += ","
        }

        roles = result
    }
}
