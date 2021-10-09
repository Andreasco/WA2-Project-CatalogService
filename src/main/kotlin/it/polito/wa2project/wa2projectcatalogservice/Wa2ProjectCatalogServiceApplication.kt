package it.polito.wa2project.wa2projectcatalogservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class Wa2ProjectCatalogServiceApplication{
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}

fun main(args: Array<String>) {
    runApplication<Wa2ProjectCatalogServiceApplication>(*args)
}
