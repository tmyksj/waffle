package waffle.domain.model

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.test.factory.WebCompositionFactory

@SpringBootTest
class WebCompositionTests {

    @Autowired
    private lateinit var webCompositionFactory: WebCompositionFactory

    @Test
    fun widthPx_must_be_in_range_from_100px_to_4000px() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webCompositionFactory.build(widthPx = 99) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webCompositionFactory.build(widthPx = 100) }
                .doesNotThrowAnyException()

            it.assertThatCode { webCompositionFactory.build(widthPx = 4000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webCompositionFactory.build(widthPx = 4001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Test
    fun delayMs_must_be_in_range_from_zero_to_1_minute() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webCompositionFactory.build(delayMs = -1) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webCompositionFactory.build(delayMs = 0) }
                .doesNotThrowAnyException()

            it.assertThatCode { webCompositionFactory.build(delayMs = 60000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webCompositionFactory.build(delayMs = 60001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

}
