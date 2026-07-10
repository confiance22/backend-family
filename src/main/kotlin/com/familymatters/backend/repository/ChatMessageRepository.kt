package com.familymatters.backend.repository

import com.familymatters.backend.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByConversationIdOrderByCreatedAtAsc(conversationId: Long): List<ChatMessage>
}
