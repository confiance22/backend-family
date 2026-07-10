package com.familymatters.backend.exception

import com.familymatters.backend.dto.StatusResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<StatusResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StatusResponse(ex.message ?: "Resource not found"))
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<StatusResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusResponse(ex.message ?: "Bad request"))
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<StatusResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(StatusResponse(ex.message ?: "Unauthorized"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<StatusResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusResponse(ex.message ?: "Invalid argument"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<StatusResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StatusResponse(errors))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<StatusResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StatusResponse("Internal server error"))
    }
}
