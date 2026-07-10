package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "daily_quotes")
class DailyQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String? = null

    @Column(nullable = false)
    var author: String? = null

    @Column(nullable = false)
    var language: String = "en"

    @Column(nullable = false, unique = true)
    var date: LocalDate? = null

    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
    }
}
