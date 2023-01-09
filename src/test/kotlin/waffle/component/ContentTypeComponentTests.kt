package waffle.component

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.InputStream

@SpringBootTest
class ContentTypeComponentTests {

    @Autowired
    private lateinit var contentTypeComponent: ContentTypeComponent

    @CsvSource(
        textBlock = """
            /Waffle_of_Japan_001.jpg, .jpg,
            /Waffle_of_Japan_001.zip, .zip,""",
    )
    @ParameterizedTest
    fun guessExtension_returns_an_extension(name: String, expected: String) {
        val stream: InputStream = checkNotNull(javaClass.getResourceAsStream(name))
        val bytes: ByteArray = stream.readAllBytes()

        val actual: String = contentTypeComponent.guessExtension(bytes)
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @CsvSource(
        textBlock = """
            /Waffle_of_Japan_001.jpg, image/jpeg,
            /Waffle_of_Japan_001.zip, application/zip,""",
    )
    @ParameterizedTest
    fun guessType_returns_a_content_type(name: String, expected: String) {
        val stream: InputStream = checkNotNull(javaClass.getResourceAsStream(name))
        val bytes: ByteArray = stream.readAllBytes()

        val actual: String = contentTypeComponent.guessType(bytes)
        Assertions.assertThat(actual).isEqualTo(expected)
    }

}
