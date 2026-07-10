package com.familymatters.backend.service

import com.familymatters.backend.dto.ConversationResponse
import com.familymatters.backend.dto.CreateConversationRequest
import com.familymatters.backend.dto.MessageResponse
import com.familymatters.backend.dto.SendMessageRequest
import com.familymatters.backend.entity.ChatConversation
import com.familymatters.backend.entity.ChatMessage
import com.familymatters.backend.repository.ChatConversationRepository
import com.familymatters.backend.repository.ChatMessageRepository
import com.familymatters.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    private val chatConversationRepository: ChatConversationRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val userRepository: UserRepository
) {
    fun createConversation(familyId: Long, request: CreateConversationRequest): ConversationResponse {
        val conversation = ChatConversation().apply {
            this.familyId = familyId
            title = request.title
        }
        chatConversationRepository.save(conversation)
        val convId = conversation.id ?: throw IllegalStateException("Conversation ID not generated")
        return ConversationResponse(
            id = convId,
            title = conversation.title,
            lastMessage = null,
            lastMessageAt = null
        )
    }

    fun getFamilyConversations(familyId: Long): List<ConversationResponse> {
        return chatConversationRepository.findByFamilyId(familyId).map { conversation ->
            val convId = conversation.id ?: throw IllegalStateException("Conversation ID not found")
            val messages = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(convId)
            val lastMessage = messages.lastOrNull()
            ConversationResponse(
                id = convId,
                title = conversation.title,
                lastMessage = lastMessage?.content,
                lastMessageAt = lastMessage?.createdAt
            )
        }
    }

    fun sendMessage(conversationId: Long, senderId: Long, request: SendMessageRequest): MessageResponse {
        val sender = userRepository.findById(senderId)
            .orElseThrow { IllegalArgumentException("Sender not found") }
        val senderName = sender.name ?: throw IllegalStateException("Sender name not found")
        val message = ChatMessage().apply {
            this.conversationId = conversationId
            this.senderId = senderId
            content = request.content
        }
        chatMessageRepository.save(message)
        val msgId = message.id ?: throw IllegalStateException("Message ID not generated")
        val msgConvId = message.conversationId ?: throw IllegalStateException("Conversation ID not found on message")
        val msgSenderId = message.senderId ?: throw IllegalStateException("Sender ID not found on message")
        val msgContent = message.content ?: throw IllegalStateException("Message content not found")
        val msgCreatedAt = message.createdAt ?: throw IllegalStateException("Message createdAt not found")
        return MessageResponse(
            id = msgId,
            conversationId = msgConvId,
            senderId = msgSenderId,
            senderName = senderName,
            content = msgContent,
            createdAt = msgCreatedAt
        )
    }

    fun getMessages(conversationId: Long): List<MessageResponse> {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).map { message ->
            val sender = userRepository.findById(message.senderId ?: throw IllegalStateException("Sender ID not found"))
                .orElseThrow { IllegalArgumentException("Sender not found") }
            val msgId = message.id ?: throw IllegalStateException("Message ID not found")
            val msgConvId = message.conversationId ?: throw IllegalStateException("Conversation ID not found")
            val msgSenderId = message.senderId ?: throw IllegalStateException("Sender ID not found")
            val msgContent = message.content ?: throw IllegalStateException("Content not found")
            val msgCreatedAt = message.createdAt ?: throw IllegalStateException("createdAt not found")
            val senderName = sender.name ?: throw IllegalStateException("Sender name not found")
            MessageResponse(
                id = msgId,
                conversationId = msgConvId,
                senderId = msgSenderId,
                senderName = senderName,
                content = msgContent,
                createdAt = msgCreatedAt
            )
        }
    }
}
