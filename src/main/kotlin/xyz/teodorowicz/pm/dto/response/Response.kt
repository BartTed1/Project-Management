package xyz.teodorowicz.pm.dto.response

data class Response<T>(
    val status: Int,
    val message: String,
    val data: T
)
