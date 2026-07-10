package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "chat_messages")
class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var conversationId: Long? = null

    @Column(nullable = false)
    var senderId: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null

    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversationId", insertable = false, updatable = false)
    var conversation: ChatConversation? = null

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
    }
}
