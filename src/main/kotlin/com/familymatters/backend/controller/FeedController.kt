package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.exception.BadRequestException
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val postService: PostService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/")
    fun getFamilyFeed(
        @RequestHeader("Authorization") authorization: String?,
        @RequestParam familyId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PostListResponse {
        getUserIdFromHeader(authorization)
        return postService.getFamilyFeed(familyId, page, size)
    }

    @PostMapping("/")
    fun createPost(
        @RequestHeader("Authorization") authorization: String?,
        @RequestHeader("X-Family-Id") familyId: Long,
        @RequestBody request: CreatePostRequest
    ): PostResponse {
        val userId = getUserIdFromHeader(authorization)
        return postService.createPost(userId, request)
    }

    @GetMapping("/my")
    fun getUserPosts(
        @RequestHeader("Authorization") authorization: String?
    ): List<PostResponse> {
        val userId = getUserIdFromHeader(authorization)
        return postService.getUserPosts(userId)
    }

    private fun getUserIdFromHeader(authorization: String?): Long {
        val token = authorization?.replace("Bearer ", "")
            ?: throw BadRequestException("Missing Authorization header")
        return jwtTokenProvider.getUserIdFromToken(token)
    }
}
