package xyz.teodorowicz.pm.config

import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import xyz.teodorowicz.pm.exception.*

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException):ResponseEntity<String> {
        return ResponseEntity.status(401).body(ex.message)
    }

    @ExceptionHandler(BadRequestException::class, IllegalArgumentException::class, ConstraintViolationException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<String?> {
        return ResponseEntity.status(400).body(ex.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<String?> {
        return ResponseEntity.status(404).body(ex.message)
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(ex: ForbiddenException): ResponseEntity<String?> {
        return ResponseEntity.status(403).body(ex.message)
    }

}
