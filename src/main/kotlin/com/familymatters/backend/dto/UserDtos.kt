package com.familymatters.backend.dto

data class UserProfileResponse(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val language: String,
    val role: String,
    val familyId: Long?
)

data class UpdateProfileRequest(
    val name: String?,
    val avatarUrl: String?,
    val language: String?
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
