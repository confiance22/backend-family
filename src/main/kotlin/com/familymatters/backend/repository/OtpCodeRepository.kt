package com.familymatters.backend.repository

import com.familymatters.backend.entity.OtpCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OtpCodeRepository : JpaRepository<OtpCode, Long> {
    fun findTopByEmailAndCodeAndUsedFalseOrderByCreatedAtDesc(email: String, code: String): OtpCode?
    fun findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email: String): OtpCode?
}
