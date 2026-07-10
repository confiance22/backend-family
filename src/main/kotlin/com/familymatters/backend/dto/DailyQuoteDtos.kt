package com.familymatters.backend.dto

data class DailyQuoteResponse(
    val id: Long,
    val content: String,
    val author: String,
    val language: String,
    val date: String
)

data class CreateQuoteRequest(
    val content: String,
    val author: String,
    val language: String,
    val date: String
)
