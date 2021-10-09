package it.polito.wa2project.wa2projectcatalogservice.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter(val jwtUtils: JwtUtils): OncePerRequestFilter() {

    @Value("\${application.jwt.jwtHeader}")
    private val jwtHeader: String? = null

    @Value("\${application.jwt.jwtHeaderStart}")
    private val jwtHeaderStart: String? = null

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                val usernameAndRolesDTO = jwtUtils.getDetailsFromJwtToken(jwt) // This will only have username and roles
                val username = usernameAndRolesDTO.username
                val authorities = usernameAndRolesDTO.authorities

                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader(jwtHeader)
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("$jwtHeaderStart ")) {
            headerAuth.substring(jwtHeaderStart!!.length + 1, headerAuth.length)
        } else null
    }
}
