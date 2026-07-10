package com.familymatters.backend.config

import com.familymatters.backend.entity.User
import com.familymatters.backend.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        val email = "confianceufitamahoro22@gmail.com"
        if (userRepository.existsByEmail(email)) {
            log.info("Seed user already exists, skipping.")
            return
        }

        val user = User().apply {
            name = "confy"
            this.email = email
            password = passwordEncoder.encode("123456")
            language = "en"
            role = User.UserRole.ADMIN
            emailVerified = true
        }
        userRepository.save(user)
        log.info("Seed user created: confy ($email)")
    }
}
