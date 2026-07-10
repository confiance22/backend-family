package com.familymatters.backend.dto

import java.time.Instant

data class CreateFamilyRequest(
    val name: String
)

data class JoinFamilyRequest(
    val inviteCode: String
)

data class FamilyResponse(
    val id: Long,
    val name: String,
    val inviteCode: String,
    val memberCount: Int
)

data class FamilyMemberResponse(
    val userId: Long,
    val name: String,
    val email: String,
    val role: String,
    val joinedAt: Instant
)
