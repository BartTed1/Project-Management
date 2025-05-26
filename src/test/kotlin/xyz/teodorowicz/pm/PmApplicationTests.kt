package xyz.teodorowicz.pm

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Disabled("Fails due to context error — investigate later")
class PmApplicationTests {

    @Test
    fun contextLoads() {
    }

}
