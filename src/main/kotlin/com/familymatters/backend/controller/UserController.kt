package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.exception.BadRequestException
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/me")
    fun getProfile(@RequestHeader("Authorization") authorization: String?): UserProfileResponse {
        val userId = getUserIdFromHeader(authorization)
        return userService.getProfile(userId)
    }

    @PutMapping("/me")
    fun updateProfile(
        @RequestHeader("Authorization") authorization: String?,
        @RequestBody request: UpdateProfileRequest
    ): UserProfileResponse {
        val userId = getUserIdFromHeader(authorization)
        return userService.updateProfile(userId, request)
    }

    @PutMapping("/me/password")
    fun changePassword(
        @RequestHeader("Authorization") authorization: String?,
        @RequestBody request: ChangePasswordRequest
    ): StatusResponse {
        val userId = getUserIdFromHeader(authorization)
        userService.changePassword(userId, request)
        return StatusResponse("Password changed successfully")
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserProfileResponse =
        userService.getProfile(id)

    private fun getUserIdFromHeader(authorization: String?): Long {
        val token = authorization?.replace("Bearer ", "")
            ?: throw BadRequestException("Missing Authorization header")
        return jwtTokenProvider.getUserIdFromToken(token)
    }
}
