package com.familymatters.backend.dto

import java.time.Instant

data class CreateConversationRequest(
    val title: String?
)

data class ConversationResponse(
    val id: Long,
    val title: String?,
    val lastMessage: String?,
    val lastMessageAt: Instant?
)

data class SendMessageRequest(
    val content: String
)

data class MessageResponse(
    val id: Long,
    val conversationId: Long,
    val senderId: Long,
    val senderName: String,
    val content: String,
    val createdAt: Instant
)

data class WebSocketMessage(
    val type: String,
    val payload: Any?
)
