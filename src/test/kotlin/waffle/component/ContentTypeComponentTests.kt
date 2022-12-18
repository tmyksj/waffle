package waffle.component

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.InputStream

@SpringBootTest
class ContentTypeComponentTests {

    @Autowired
    private lateinit var contentTypeComponent: ContentTypeComponent

    @Test
    fun guess_returns_a_content_type() {
        val stream: InputStream = checkNotNull(javaClass.getResourceAsStream("/Waffle_of_Japan_001.jpg"))
        val bytes: ByteArray = stream.readAllBytes()

        val actual: String? = contentTypeComponent.guess(bytes)
        Assertions.assertThat(actual).isEqualTo("image/jpeg")
    }

}
