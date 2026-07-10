package com.familymatters.backend.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "family_members",
    uniqueConstraints = [UniqueConstraint(columnNames = ["familyId", "userId"])]
)
class FamilyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var familyId: Long? = null

    @Column(nullable = false)
    var userId: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: MemberRole = MemberRole.CHILD

    @Column(nullable = false, updatable = false)
    var joinedAt: Instant? = null

    @PrePersist
    fun prePersist() {
        joinedAt = Instant.now()
    }

    enum class MemberRole {
        PARENT, CHILD, GRANDPARENT, OTHER
    }
}
