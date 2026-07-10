package com.familymatters.backend.service

import com.familymatters.backend.dto.CreateFamilyRequest
import com.familymatters.backend.dto.FamilyMemberResponse
import com.familymatters.backend.dto.FamilyResponse
import com.familymatters.backend.dto.JoinFamilyRequest
import com.familymatters.backend.entity.Family
import com.familymatters.backend.entity.FamilyMember
import com.familymatters.backend.repository.FamilyMemberRepository
import com.familymatters.backend.repository.FamilyRepository
import com.familymatters.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class FamilyService(
    private val familyRepository: FamilyRepository,
    private val familyMemberRepository: FamilyMemberRepository,
    private val userRepository: UserRepository
) {
    fun createFamily(userId: Long, request: CreateFamilyRequest): FamilyResponse {
        var inviteCode: String
        do {
            inviteCode = UUID.randomUUID().toString().take(8)
        } while (familyRepository.existsByInviteCode(inviteCode))

        val family = Family().apply {
            name = request.name
            this.inviteCode = inviteCode
        }
        familyRepository.save(family)

        val familyId = family.id ?: throw IllegalStateException("Family ID not generated")

        val member = FamilyMember().apply {
            this.familyId = familyId
            this.userId = userId
            role = FamilyMember.MemberRole.PARENT
        }
        familyMemberRepository.save(member)

        return FamilyResponse(
            id = familyId,
            name = family.name ?: throw IllegalStateException("Family name not found"),
            inviteCode = family.inviteCode ?: throw IllegalStateException("Invite code not found"),
            memberCount = 1
        )
    }

    fun joinFamily(userId: Long, request: JoinFamilyRequest): FamilyResponse {
        val family = familyRepository.findByInviteCode(request.inviteCode)
            ?: throw IllegalArgumentException("Invalid invite code")
        val familyId = family.id ?: throw IllegalStateException("Family ID not found")
        if (familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId)) {
            throw IllegalArgumentException("Already a member of this family")
        }
        val member = FamilyMember().apply {
            this.familyId = familyId
            this.userId = userId
            role = FamilyMember.MemberRole.CHILD
        }
        familyMemberRepository.save(member)
        val memberCount = familyMemberRepository.findByFamilyId(familyId).size
        return FamilyResponse(
            id = familyId,
            name = family.name ?: throw IllegalStateException("Family name not found"),
            inviteCode = family.inviteCode ?: throw IllegalStateException("Invite code not found"),
            memberCount = memberCount
        )
    }

    fun getFamily(familyId: Long): FamilyResponse {
        val family = familyRepository.findById(familyId).orElseThrow { IllegalArgumentException("Family not found") }
        val famId = family.id ?: throw IllegalStateException("Family ID not found")
        val memberCount = familyMemberRepository.findByFamilyId(famId).size
        return FamilyResponse(
            id = famId,
            name = family.name ?: throw IllegalStateException("Family name not found"),
            inviteCode = family.inviteCode ?: throw IllegalStateException("Invite code not found"),
            memberCount = memberCount
        )
    }

    fun getFamilyMembers(familyId: Long): List<FamilyMemberResponse> {
        val members = familyMemberRepository.findByFamilyId(familyId)
        return members.map { member ->
            val userId = member.userId ?: throw IllegalStateException("User ID not found")
            val user = userRepository.findById(userId)
                .orElseThrow { IllegalArgumentException("User not found") }
            FamilyMemberResponse(
                userId = userId,
                name = user.name ?: throw IllegalStateException("User name not found"),
                email = user.email ?: throw IllegalStateException("User email not found"),
                role = member.role.name,
                joinedAt = member.joinedAt ?: throw IllegalStateException("JoinedAt not found")
            )
        }
    }

    fun getUserFamilies(userId: Long): List<FamilyResponse> {
        val memberships = familyMemberRepository.findByUserId(userId)
        return memberships.map { membership ->
            val famId = membership.familyId ?: throw IllegalStateException("Family ID not found")
            val family = familyRepository.findById(famId)
                .orElseThrow { IllegalArgumentException("Family not found") }
            val fid = family.id ?: throw IllegalStateException("Family ID not found")
            val memberCount = familyMemberRepository.findByFamilyId(fid).size
            FamilyResponse(
                id = fid,
                name = family.name ?: throw IllegalStateException("Family name not found"),
                inviteCode = family.inviteCode ?: throw IllegalStateException("Invite code not found"),
                memberCount = memberCount
            )
        }
    }
}
