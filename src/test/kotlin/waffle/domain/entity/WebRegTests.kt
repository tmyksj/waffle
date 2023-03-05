package waffle.domain.entity

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.core.time.now
import waffle.core.type.Blob
import waffle.test.factory.WebRegFactory
import java.time.LocalDateTime

@SpringBootTest
class WebRegTests {

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @MethodSource
    @ParameterizedTest
    fun isReady(state: WebReg.State, expected: Boolean) {
        Assertions.assertThat(webRegFactory.build(state = state).isReady).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isStarted(state: WebReg.State, expected: Boolean) {
        Assertions.assertThat(webRegFactory.build(state = state).isStarted).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isCompleted(state: WebReg.State, expected: Boolean) {
        Assertions.assertThat(webRegFactory.build(state = state).isCompleted).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isFailed(state: WebReg.State, expected: Boolean) {
        Assertions.assertThat(webRegFactory.build(state = state).isFailed).isEqualTo(expected)
    }

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
        val result = Blob()
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

    companion object {

        @JvmStatic
        fun isReady(): List<Arguments> {
            return listOf(
                Arguments.of(WebReg.State.Ready, true),
                Arguments.of(WebReg.State.Started, false),
                Arguments.of(WebReg.State.Completed, false),
                Arguments.of(WebReg.State.Failed, false),
            )
        }

        @JvmStatic
        fun isStarted(): List<Arguments> {
            return listOf(
                Arguments.of(WebReg.State.Ready, false),
                Arguments.of(WebReg.State.Started, true),
                Arguments.of(WebReg.State.Completed, false),
                Arguments.of(WebReg.State.Failed, false),
            )
        }

        @JvmStatic
        fun isCompleted(): List<Arguments> {
            return listOf(
                Arguments.of(WebReg.State.Ready, false),
                Arguments.of(WebReg.State.Started, false),
                Arguments.of(WebReg.State.Completed, true),
                Arguments.of(WebReg.State.Failed, false),
            )
        }

        @JvmStatic
        fun isFailed(): List<Arguments> {
            return listOf(
                Arguments.of(WebReg.State.Ready, false),
                Arguments.of(WebReg.State.Started, false),
                Arguments.of(WebReg.State.Completed, false),
                Arguments.of(WebReg.State.Failed, true),
            )
        }

    }

}
