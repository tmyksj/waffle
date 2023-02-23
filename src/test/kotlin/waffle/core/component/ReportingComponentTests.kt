package waffle.core.component

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.InputStream

@SpringBootTest
class ReportingComponentTests {

    @Autowired
    private lateinit var reportingComponent: ReportingComponent

    @Test
    fun compareImages_returns_bytes() {
        val stream: InputStream = checkNotNull(javaClass.getResourceAsStream("/Waffle_of_Japan_001.jpg"))
        val bytes: ByteArray = stream.readAllBytes()

        val actual: ByteArray = reportingComponent.compareImages(listOf(bytes), listOf(bytes))
        Assertions.assertThat(actual.size).isNotZero
    }

}
