package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.AuthService
import com.familymatters.backend.service.OtpService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val otpService: OtpService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): AuthResponse =
        authService.register(request)

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthResponse =
        authService.login(request)

    @PostMapping("/verify-otp")
    fun verifyOtp(
        @RequestBody request: OtpVerifyRequest,
        @RequestHeader("Authorization") authorization: String?
    ): StatusResponse {
        otpService.verifyOtp(request.email, request.code)
        return StatusResponse("Email verified successfully")
    }

    @PostMapping("/resend-otp")
    fun resendOtp(@RequestBody request: OtpResendRequest): StatusResponse {
        otpService.generateOtp(request.email)
        return StatusResponse("Code resent")
    }
}
