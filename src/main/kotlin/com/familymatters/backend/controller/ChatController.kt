package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.exception.BadRequestException
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.ChatService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/conversations")
    fun getConversations(
        @RequestHeader("Authorization") authorization: String?,
        @RequestParam familyId: Long
    ): List<ConversationResponse> {
        getUserIdFromHeader(authorization)
        return chatService.getFamilyConversations(familyId)
    }

    @PostMapping("/conversations")
    fun createConversation(
        @RequestHeader("Authorization") authorization: String?,
        @RequestHeader("X-Family-Id") familyId: Long,
        @RequestBody request: CreateConversationRequest
    ): ConversationResponse {
        val userId = getUserIdFromHeader(authorization)
        return chatService.createConversation(familyId, request)
    }

    @GetMapping("/conversations/{conversationId}/messages")
    fun getMessages(
        @RequestHeader("Authorization") authorization: String?,
        @PathVariable conversationId: Long
    ): List<MessageResponse> {
        getUserIdFromHeader(authorization)
        return chatService.getMessages(conversationId)
    }

    @PostMapping("/conversations/{conversationId}/messages")
    fun sendMessage(
        @RequestHeader("Authorization") authorization: String?,
        @PathVariable conversationId: Long,
        @RequestBody request: SendMessageRequest
    ): MessageResponse {
        val userId = getUserIdFromHeader(authorization)
        return chatService.sendMessage(conversationId, userId, request)
    }

    private fun getUserIdFromHeader(authorization: String?): Long {
        val token = authorization?.replace("Bearer ", "")
            ?: throw BadRequestException("Missing Authorization header")
        return jwtTokenProvider.getUserIdFromToken(token)
    }
}
