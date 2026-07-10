package com.familymatters.backend.repository

import com.familymatters.backend.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findByFamilyIdOrderByCreatedAtDesc(familyId: Long): List<Post>
    fun findByAuthorIdOrderByCreatedAtDesc(authorId: Long): List<Post>
}
