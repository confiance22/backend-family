package com.familymatters.backend.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String
)

data class OtpVerifyRequest(
    val email: String,
    val code: String
)

data class OtpResendRequest(
    val email: String
)

data class StatusResponse(
    val message: String
)
