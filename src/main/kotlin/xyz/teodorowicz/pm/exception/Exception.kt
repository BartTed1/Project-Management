package xyz.teodorowicz.pm.exception

class UnauthorizedException(message: String? = null) : RuntimeException(message)
class BadRequestException(message: String? = null) : RuntimeException(message)
class NotFoundException(message: String? = null) : RuntimeException(message)
class ForbiddenException(message: String? = null) : RuntimeException(message)
class InternalServerErrorException(message: String? = null) : RuntimeException(message)