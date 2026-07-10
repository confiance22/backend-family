package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "posts")
class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var authorId: Long? = null

    @Column(nullable = false)
    var familyId: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null

    @Column
    var mediaUrls: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: PostType = PostType.MEMORY

    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @Column(nullable = false)
    var updatedAt: Instant? = null

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
        updatedAt = Instant.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Instant.now()
    }

    enum class PostType {
        MEMORY, MILESTONE, STORY, PHOTO
    }
}
