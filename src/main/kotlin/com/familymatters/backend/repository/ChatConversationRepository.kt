package com.familymatters.backend.repository

import com.familymatters.backend.entity.ChatConversation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatConversationRepository : JpaRepository<ChatConversation, Long> {
    fun findByFamilyId(familyId: Long): List<ChatConversation>
}
