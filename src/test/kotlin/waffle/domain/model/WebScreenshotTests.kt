package waffle.domain.model

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.test.factory.WebScreenshotFactory

@SpringBootTest
class WebScreenshotTests {

    @Autowired
    private lateinit var webScreenshotFactory: WebScreenshotFactory

    @Test
    fun widthPx_must_be_in_range_from_100px_to_4000px() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webScreenshotFactory.build(widthPx = 99) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webScreenshotFactory.build(widthPx = 100) }
                .doesNotThrowAnyException()

            it.assertThatCode { webScreenshotFactory.build(widthPx = 4000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webScreenshotFactory.build(widthPx = 4001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

}
