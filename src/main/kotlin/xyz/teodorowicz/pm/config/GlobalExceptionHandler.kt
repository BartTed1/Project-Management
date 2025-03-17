package xyz.teodorowicz.pm.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.exception.BadRequestException
import xyz.teodorowicz.pm.exception.NotFoundException
import xyz.teodorowicz.pm.exception.UnauthorizedException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<Response<Nothing?>> {
        return ResponseEntity.status(401).body(
            Response(
                status = 401,
                message = ex.message ?: "Nieautoryzowany dostęp",
                data = null
            )
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<Response<Nothing?>> {
        return ResponseEntity.status(400).body(
            Response(
                status = 400,
                message = ex.message ?: "Nieprawidłowe żądanie",
                data = null
            )
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<Response<Nothing?>> {
        return ResponseEntity.status(404).body(
            Response(
                status = 404,
                message = ex.message ?: "Nie znaleziono",
                data = null
            )
        )
    }
}
