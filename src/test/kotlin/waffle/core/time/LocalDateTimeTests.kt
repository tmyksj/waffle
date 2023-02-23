package waffle.core.time

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class LocalDateTimeTests {

    @Test
    fun now_returns_LocalDateTime_with_a_microseconds_precision() {
        val a: LocalDateTime = LocalDateTime.now()
            .let { it.minusNanos(it.nano % 1000L) }
        val b: LocalDateTime = now()
        val c: LocalDateTime = LocalDateTime.now()

        SoftAssertions.assertSoftly {
            it.assertThat(b).isAfterOrEqualTo(a)
            it.assertThat(b).isBeforeOrEqualTo(c)
            it.assertThat(b.nano % 1000).isEqualTo(0)
        }
    }

}
