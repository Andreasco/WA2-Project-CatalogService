package it.polito.wa2project.wa2projectcatalogservice.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import it.polito.wa2project.wa2projectcatalogservice.dto.auth.UserDetailsDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.xml.bind.DatatypeConverter

@Component
class JwtUtils {
    private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    private var signingKey: SecretKey? = null

    @Value("\${application.jwt.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${application.jwt.jwtExpirationMs}")
    private val jwtExpirationMs: Int? = null

    @PostConstruct
    private fun createSigningKey() {
        val keyBytes = DatatypeConverter.parseBase64Binary(jwtSecret)
        signingKey = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateJwtToken(authentication: Authentication): String {
        val userDetailsDTO = authentication.principal as UserDetailsDTO

        return Jwts.builder()
            .setSubject(userDetailsDTO.username)
            .claim("roles", userDetailsDTO.getRoles()) // I don't pass the authorities because when I construct the UserDetailsDTO in getDetailsFromJwtToken the roles would have "ROLE_" as prefix
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs!!))
            //.signWith(SignatureAlgorithm.HS512, jwtSecret) deprecated
            .signWith(signingKey)
            .compact()
    }

    // FOR TEST ONLY
    fun generateJwtToken(userDetailsDTO: UserDetailsDTO): String {
        return Jwts.builder()
            .setSubject(userDetailsDTO.username)
            .claim("roles", userDetailsDTO.getRoles())
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs!!))
            //.signWith(SignatureAlgorithm.HS512, jwtSecret) deprecated
            .signWith(signingKey)
            .compact()
    }

    fun validateJwtToken(authToken: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(authToken)

            //Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken) deprecated
            true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature: {}", e.message)
            false
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
            false
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
            false
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
            false
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
            false
        }

        //return false
    }

    fun getDetailsFromJwtToken(authToken: String): UserDetailsDTO {
        val jwtClaims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(authToken).body
        val username = jwtClaims.subject
        val roles = jwtClaims["roles"]

        return UserDetailsDTO(username, roles = roles.toString())
    }
}
