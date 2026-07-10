package com.familymatters.backend.repository

import com.familymatters.backend.entity.Family
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FamilyRepository : JpaRepository<Family, Long> {
    fun findByInviteCode(inviteCode: String): Family?
    fun existsByInviteCode(inviteCode: String): Boolean
}
