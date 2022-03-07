package it.polito.wa2project.wa2projectcatalogservice

import it.polito.wa2project.wa2projectcatalogservice.repositories.CustomerRepository
import it.polito.wa2project.wa2projectcatalogservice.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true) // This enables global Method Security like pre/post annotations in services
class Wa2ProjectCatalogServiceApplication{
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    /********************** TESTS ********************/

    @Bean
    fun test(
        customerRepository: CustomerRepository,
        userRepository: UserRepository
    ): CommandLineRunner {
        return CommandLineRunner {

            //initializationTest(customerRepository, walletRepository, userRepository)

        }
    }

    /**
     * Initialization test.
     *
     * Use this function to create 2 customers with 2
     * associated wallets each.
     * This part must be used one-time only, then you can
     * perform further tests on the created entities.
     */

    /*fun initializationTest(
        customerRepository: CustomerRepository,
        walletRepository: WalletRepository,
        userRepository: UserRepository
    ) {
        val c1 = Customer(
            "Guido",
            "Ricioppo",
            "Via Saluzzo 13",
            "gng2@gmail.com",
            null
        )

        val c2 = Customer(
            "Andrea",
            "Scopp",
            "Lamezia",
            "andrea.scopp@gmail.com",
            null
        )

        val user1 = User(
            "guidonguido",
            "p1",
            c1.email,
            c1
        )

        val user2 = User(
            "andreascopp",
            "p2",
            c2.email,
            c2
        )

        user1.addRolename(Rolename.ADMIN)
        user1.addRolename(Rolename.CUSTOMER)

        user2.addRolename(Rolename.CUSTOMER)

        c1.user = user1
        c2.user = user2

        userRepository.save(user1)
        userRepository.save(user2)

        println("[TEST] After Users creation")
        print("User: {id: " +user1.getId() +" rolenames: " +user1.getRolenames() +"}\n")

        user1.removeRolename(Rolename.CUSTOMER)
        userRepository.save(user1)

        println("[TEST] After removing the CUSTOMER role")
        print("User: {id: " +user1.getId() +" rolenames: " +user1.getRolenames() +"}\n")

        val customer1 = customerRepository.findById(c1.getId()!!)
        val customer2 = customerRepository.findById(c2.getId()!!)


        val w11 = Wallet(customer1.get(), 100.0)
        val w12 = Wallet(customer1.get())
        walletRepository.save(w11)
        walletRepository.save(w12)

        // val customer2 = customerRepository.findById(1)
        val w21 = Wallet(customer2.get(), 20.0)
        val w22 = Wallet(customer2.get())
        walletRepository.save(w21)
        walletRepository.save(w22)
    }*/
}

fun main(args: Array<String>) {
    runApplication<Wa2ProjectCatalogServiceApplication>(*args)
}
