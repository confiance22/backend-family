package com.familymatters.backend.repository

import com.familymatters.backend.entity.DailyQuote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyQuoteRepository : JpaRepository<DailyQuote, Long> {
    fun findByDate(date: LocalDate): DailyQuote?
    fun findByLanguage(language: String): List<DailyQuote>
}
