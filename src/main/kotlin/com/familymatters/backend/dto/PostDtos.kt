package com.familymatters.backend.dto

import java.time.Instant

data class CreatePostRequest(
    val content: String,
    val mediaUrls: List<String>?,
    val type: String
)

data class PostResponse(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val content: String,
    val mediaUrls: List<String>?,
    val type: String,
    val createdAt: Instant
)

data class PostListResponse(
    val posts: List<PostResponse>,
    val hasMore: Boolean
)
