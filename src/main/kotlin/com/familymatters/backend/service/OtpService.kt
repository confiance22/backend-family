package com.familymatters.backend.service

import com.familymatters.backend.entity.OtpCode
import com.familymatters.backend.repository.OtpCodeRepository
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@Transactional
class OtpService(
    private val otpCodeRepository: OtpCodeRepository,
    private val mailSender: JavaMailSender,
    @Value("\${app.otp.expiration-minutes}") private val expirationMinutes: Int
) {
    fun generateOtp(email: String): String {
        val code = String.format("%06d", Random().nextInt(999999))
        val otp = OtpCode().apply {
            this.email = email
            this.code = code
            this.expiresAt = Instant.now().plusSeconds(expirationMinutes * 60L)
        }
        otpCodeRepository.save(otp)
        sendOtpEmail(email, code)
        return code
    }

    private fun sendOtpEmail(email: String, code: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setTo(email)
        helper.setSubject("Family Matters - Your Verification Code")
        helper.setText(
            """
            <html>
            <body>
                <h2>Family Matters Email Verification</h2>
                <p>Your verification code is:</p>
                <h1 style="letter-spacing: 8px; font-size: 32px; color: #4F46E5;">$code</h1>
                <p>This code will expire in $expirationMinutes minutes.</p>
            </body>
            </html>
            """.trimIndent(),
            true
        )
        mailSender.send(message)
    }

    fun verifyOtp(email: String, code: String): Boolean {
        val otp = otpCodeRepository.findTopByEmailAndCodeAndUsedFalseOrderByCreatedAtDesc(email, code) ?: return false
        val expiresAt = otp.expiresAt ?: return false
        if (expiresAt.isBefore(Instant.now())) return false
        otp.used = true
        otpCodeRepository.save(otp)
        return true
    }
}
