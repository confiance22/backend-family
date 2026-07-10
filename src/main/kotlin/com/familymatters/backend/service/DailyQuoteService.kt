package com.familymatters.backend.service

import com.familymatters.backend.dto.CreateQuoteRequest
import com.familymatters.backend.dto.DailyQuoteResponse
import com.familymatters.backend.entity.DailyQuote
import com.familymatters.backend.repository.DailyQuoteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class DailyQuoteService(
    private val dailyQuoteRepository: DailyQuoteRepository
) {
    fun getTodaysQuote(language: String): DailyQuoteResponse? {
        val quote = dailyQuoteRepository.findByDate(LocalDate.now()) ?: return null
        if (quote.language != language) return null
        return mapToResponse(quote)
    }

    fun createQuote(request: CreateQuoteRequest): DailyQuoteResponse {
        val quote = DailyQuote().apply {
            content = request.content
            author = request.author
            language = request.language
            date = LocalDate.parse(request.date)
        }
        dailyQuoteRepository.save(quote)
        return mapToResponse(quote)
    }

    fun getAllQuotes(language: String): List<DailyQuoteResponse> {
        return dailyQuoteRepository.findByLanguage(language).map { mapToResponse(it) }
    }

    private fun mapToResponse(quote: DailyQuote): DailyQuoteResponse {
        return DailyQuoteResponse(
            id = quote.id!!,
            content = quote.content!!,
            author = quote.author!!,
            language = quote.language,
            date = quote.date.toString()
        )
    }
}
