package com.familymatters.backend.service

import com.familymatters.backend.dto.AuthResponse
import com.familymatters.backend.dto.LoginRequest
import com.familymatters.backend.dto.RegisterRequest
import com.familymatters.backend.entity.User
import com.familymatters.backend.repository.UserRepository
import com.familymatters.backend.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val otpService: OtpService
) {
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already registered")
        }
        val user = User().apply {
            name = request.name
            email = request.email
            password = passwordEncoder.encode(request.password)
        }
        userRepository.save(user)
        otpService.generateOtp(user.email!!)
        val token = jwtTokenProvider.generateToken(user.id!!, user.email!!)
        return AuthResponse(
            token = token,
            userId = user.id!!,
            name = user.name!!,
            email = user.email!!
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email) ?: throw IllegalArgumentException("Invalid credentials")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid credentials")
        }
        val token = jwtTokenProvider.generateToken(user.id!!, user.email!!)
        return AuthResponse(
            token = token,
            userId = user.id!!,
            name = user.name!!,
            email = user.email!!
        )
    }
}
