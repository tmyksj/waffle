package waffle.domain.model

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.test.factory.WebSnapshotFactory

@SpringBootTest
class WebSnapshotTests {

    @Autowired
    private lateinit var webSnapshotFactory: WebSnapshotFactory

    @Test
    fun widthPx_must_be_in_range_from_100px_to_4000px() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webSnapshotFactory.build(widthPx = 99) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webSnapshotFactory.build(widthPx = 100) }
                .doesNotThrowAnyException()

            it.assertThatCode { webSnapshotFactory.build(widthPx = 4000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webSnapshotFactory.build(widthPx = 4001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Test
    fun heightPx_must_be_in_range_from_100px_to_4000px() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webSnapshotFactory.build(heightPx = 99) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webSnapshotFactory.build(heightPx = 100) }
                .doesNotThrowAnyException()

            it.assertThatCode { webSnapshotFactory.build(heightPx = 4000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webSnapshotFactory.build(heightPx = 4001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

}
