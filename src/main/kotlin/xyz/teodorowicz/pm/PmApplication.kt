package xyz.teodorowicz.pm

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@OpenAPIDefinition(
    info = Info(
        title = "PM API",
        version = "1.0",
        description = "Dokumentacja API"
    )
)
@EntityScan(basePackages = ["xyz.teodorowicz.pm.entity"])
@SpringBootApplication
class PmApplication

fun main(args: Array<String>) {
    runApplication<PmApplication>(*args)
}
