package com.familymatters.backend.service

import com.familymatters.backend.dto.ChangePasswordRequest
import com.familymatters.backend.dto.UpdateProfileRequest
import com.familymatters.backend.dto.UserProfileResponse
import com.familymatters.backend.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun getProfile(userId: Long): UserProfileResponse {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        return UserProfileResponse(
            id = user.id!!,
            name = user.name!!,
            email = user.email!!,
            avatarUrl = user.avatarUrl,
            language = user.language,
            role = user.role.name,
            familyId = user.familyId
        )
    }

    fun updateProfile(userId: Long, request: UpdateProfileRequest): UserProfileResponse {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        request.name?.let { user.name = it }
        request.avatarUrl?.let { user.avatarUrl = it }
        request.language?.let { user.language = it }
        userRepository.save(user)
        return UserProfileResponse(
            id = user.id!!,
            name = user.name!!,
            email = user.email!!,
            avatarUrl = user.avatarUrl,
            language = user.language,
            role = user.role.name,
            familyId = user.familyId
        )
    }

    fun changePassword(userId: Long, request: ChangePasswordRequest) {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw IllegalArgumentException("Current password is incorrect")
        }
        user.password = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }
}
