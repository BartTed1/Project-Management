package xyz.teodorowicz.pm.config

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.exception.*

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
                message = "Nie znaleziono",
                data = null
            )
        )
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(ex: ForbiddenException): ResponseEntity<Response<Nothing?>> {
        return ResponseEntity.status(403).body(
            Response(
                status = 403,
                message = "Zabroniony dostęp",
                data = null
            )
        )
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleInternalServerError(ex: InternalServerErrorException): ResponseEntity<Response<Nothing?>> {
        return ResponseEntity.status(500).body(
            Response(
                status = 500,
                message = "Wewnętrzny błąd serwera",
                data = null
            )
        )
    }
}
