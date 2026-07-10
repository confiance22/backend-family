package com.familymatters.backend.repository

import com.familymatters.backend.entity.FamilyMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FamilyMemberRepository : JpaRepository<FamilyMember, Long> {
    fun findByFamilyId(familyId: Long): List<FamilyMember>
    fun findByUserId(userId: Long): List<FamilyMember>
    fun existsByFamilyIdAndUserId(familyId: Long, userId: Long): Boolean
}
