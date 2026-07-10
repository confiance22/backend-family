package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.exception.BadRequestException
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.FamilyService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/families")
class FamilyController(
    private val familyService: FamilyService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/")
    fun createFamily(
        @RequestHeader("Authorization") authorization: String?,
        @RequestBody request: CreateFamilyRequest
    ): FamilyResponse {
        val userId = getUserIdFromHeader(authorization)
        return familyService.createFamily(userId, request)
    }

    @PostMapping("/join")
    fun joinFamily(
        @RequestHeader("Authorization") authorization: String?,
        @RequestBody request: JoinFamilyRequest
    ): FamilyResponse {
        val userId = getUserIdFromHeader(authorization)
        return familyService.joinFamily(userId, request)
    }

    @GetMapping("/")
    fun getUserFamilies(
        @RequestHeader("Authorization") authorization: String?
    ): List<FamilyResponse> {
        val userId = getUserIdFromHeader(authorization)
        return familyService.getUserFamilies(userId)
    }

    @GetMapping("/{familyId}")
    fun getFamily(@PathVariable familyId: Long): FamilyResponse =
        familyService.getFamily(familyId)

    @GetMapping("/{familyId}/members")
    fun getFamilyMembers(@PathVariable familyId: Long): List<FamilyMemberResponse> =
        familyService.getFamilyMembers(familyId)

    private fun getUserIdFromHeader(authorization: String?): Long {
        val token = authorization?.replace("Bearer ", "")
            ?: throw BadRequestException("Missing Authorization header")
        return jwtTokenProvider.getUserIdFromToken(token)
    }
}
