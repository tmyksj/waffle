package waffle.domain.entity

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.time.now
import waffle.test.factory.WebRegFactory
import java.time.LocalDateTime

@SpringBootTest
class WebRegTests {

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun transition_to_the_state_Started() {
        val now: LocalDateTime = now()

        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.start()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebReg.State.Started)
            it.assertThat(actual.startedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Completed() {
        val result = ByteArray(0)
        val now: LocalDateTime = now()

        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.complete(result)

        SoftAssertions.assertSoftly {
            it.assertThat(actual.result).isEqualTo(result)
            it.assertThat(actual.state).isEqualTo(WebReg.State.Completed)
            it.assertThat(actual.completedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Failed() {
        val now: LocalDateTime = now()

        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.fail()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebReg.State.Failed)
            it.assertThat(actual.failedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun delayMs_must_be_in_range_from_zero_to_1_minute() {
        SoftAssertions.assertSoftly {
            it.assertThatThrownBy { webRegFactory.buildComposition(delayMs = -1) }
                .isInstanceOf(IllegalArgumentException::class.java)

            it.assertThatCode { webRegFactory.buildComposition(delayMs = 0) }
                .doesNotThrowAnyException()

            it.assertThatCode { webRegFactory.buildComposition(delayMs = 60000) }
                .doesNotThrowAnyException()

            it.assertThatThrownBy { webRegFactory.buildComposition(delayMs = 60001) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

}
