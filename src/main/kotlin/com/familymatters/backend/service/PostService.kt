package com.familymatters.backend.service

import com.familymatters.backend.dto.CreatePostRequest
import com.familymatters.backend.dto.PostListResponse
import com.familymatters.backend.dto.PostResponse
import com.familymatters.backend.entity.Post
import com.familymatters.backend.repository.FamilyMemberRepository
import com.familymatters.backend.repository.PostRepository
import com.familymatters.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val familyMemberRepository: FamilyMemberRepository,
    private val userRepository: UserRepository
) {
    fun createPost(authorId: Long, request: CreatePostRequest): PostResponse {
        val familyId = userRepository.findById(authorId)
            .orElseThrow { IllegalArgumentException("User not found") }
            .familyId ?: throw IllegalArgumentException("User does not belong to a family")
        if (!familyMemberRepository.existsByFamilyIdAndUserId(familyId, authorId)) {
            throw IllegalArgumentException("User is not a member of this family")
        }
        val post = Post().apply {
            this.authorId = authorId
            this.familyId = familyId
            content = request.content
            mediaUrls = request.mediaUrls?.joinToString(",")
            type = Post.PostType.valueOf(request.type.uppercase())
        }
        postRepository.save(post)
        return mapToPostResponse(post)
    }

    fun getFamilyFeed(familyId: Long, page: Int, size: Int): PostListResponse {
        val posts = postRepository.findByFamilyIdOrderByCreatedAtDesc(familyId)
        val start = page * size
        val end = (start + size).coerceAtMost(posts.size)
        val hasMore = end < posts.size
        val pagePosts = if (start < posts.size) posts.subList(start, end) else emptyList()
        val postResponses = pagePosts.map { mapToPostResponse(it) }
        return PostListResponse(posts = postResponses, hasMore = hasMore)
    }

    fun getUserPosts(authorId: Long): List<PostResponse> {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId).map { mapToPostResponse(it) }
    }

    private fun mapToPostResponse(post: Post): PostResponse {
        val author = userRepository.findById(post.authorId!!)
            .orElseThrow { IllegalArgumentException("Author not found") }
        return PostResponse(
            id = post.id!!,
            authorId = post.authorId!!,
            authorName = author.name!!,
            content = post.content!!,
            mediaUrls = post.mediaUrls?.split(",")?.map { it.trim() },
            type = post.type.name,
            createdAt = post.createdAt!!
        )
    }
}
