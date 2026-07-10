package com.familymatters.backend.controller

import com.familymatters.backend.dto.*
import com.familymatters.backend.exception.BadRequestException
import com.familymatters.backend.security.JwtTokenProvider
import com.familymatters.backend.service.DailyQuoteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quotes")
class DailyQuoteController(
    private val dailyQuoteService: DailyQuoteService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/today")
    fun getTodaysQuote(
        @RequestHeader("Authorization") authorization: String?,
        @RequestParam(defaultValue = "en") language: String
    ): DailyQuoteResponse? {
        getUserIdFromHeader(authorization)
        return dailyQuoteService.getTodaysQuote(language)
    }

    @GetMapping("/")
    fun getAllQuotes(
        @RequestHeader("Authorization") authorization: String?,
        @RequestParam(defaultValue = "en") language: String
    ): List<DailyQuoteResponse> {
        getUserIdFromHeader(authorization)
        return dailyQuoteService.getAllQuotes(language)
    }

    @PostMapping("/")
    fun createQuote(
        @RequestHeader("Authorization") authorization: String?,
        @RequestBody request: CreateQuoteRequest
    ): DailyQuoteResponse {
        getUserIdFromHeader(authorization)
        return dailyQuoteService.createQuote(request)
    }

    private fun getUserIdFromHeader(authorization: String?): Long {
        val token = authorization?.replace("Bearer ", "")
            ?: throw BadRequestException("Missing Authorization header")
        return jwtTokenProvider.getUserIdFromToken(token)
    }
}
