package it.polito.wa2project.wa2projectcatalogservice.dto.auth


import it.polito.wa2project.wa2projectcatalogservice.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsDTO(
    private var username: String, //I need to make it private because otherwise it would create a getter that would be in conflict with the one in UserDetails
    private var password: String? = null,
    var email: String? = null,
    var customerId: Long? = null,
    private var isEnabled: Boolean? = null,
    private var roles: String
): UserDetails {

    private fun getRolenames(): Set<String> =
        roles.split(",").toSet()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        getRolenames().map { SimpleGrantedAuthority("ROLE_$it") }.toMutableSet()

    override fun getPassword(): String? = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = isEnabled ?: false

    fun getRoles(): String = roles
}

fun User.toUserDetailsDTO() = UserDetailsDTO(
    username,
    password,
    email,
    customer.getId(),
    isEnabled,
    roles
)
