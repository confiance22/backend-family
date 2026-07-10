package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "families")
class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false, unique = true, length = 8)
    var inviteCode: String? = null

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
}
