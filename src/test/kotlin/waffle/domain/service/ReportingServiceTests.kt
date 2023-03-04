package waffle.domain.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.InputStream

@SpringBootTest
class ReportingServiceTests {

    @Autowired
    private lateinit var reportingService: ReportingService

    @Test
    fun compareImages_returns_bytes() {
        val stream: InputStream = checkNotNull(javaClass.getResourceAsStream("/Waffle_of_Japan_001.jpg"))
        val bytes: ByteArray = stream.readAllBytes()

        val actual: ByteArray = reportingService.compareImages(listOf(bytes), listOf(bytes))
        Assertions.assertThat(actual.size).isNotZero
    }

}
