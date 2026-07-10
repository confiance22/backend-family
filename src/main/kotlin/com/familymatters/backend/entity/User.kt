package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false, unique = true)
    var email: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column
    var avatarUrl: String? = null

    @Column(nullable = false)
    var language: String = "en"

    @Column
    var familyId: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.MEMBER

    @Column(nullable = false)
    var emailVerified: Boolean = false

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

    enum class UserRole {
        MEMBER, ADMIN
    }
}
