package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "otp_codes")
class OtpCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var email: String? = null

    @Column(nullable = false, length = 6)
    var code: String? = null

    @Column(nullable = false)
    var expiresAt: Instant? = null

    @Column(nullable = false)
    var used: Boolean = false

    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
    }
}
