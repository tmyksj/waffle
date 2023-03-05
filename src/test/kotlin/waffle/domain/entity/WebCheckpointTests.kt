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
import waffle.domain.model.WebSnapshot
import waffle.test.factory.WebCheckpointFactory
import waffle.test.factory.WebSnapshotFactory
import java.time.LocalDateTime

@SpringBootTest
class WebCheckpointTests {

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var webSnapshotFactory: WebSnapshotFactory

    @MethodSource
    @ParameterizedTest
    fun isReady(state: WebCheckpoint.State, expected: Boolean) {
        Assertions.assertThat(webCheckpointFactory.build(state = state).isReady).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isStarted(state: WebCheckpoint.State, expected: Boolean) {
        Assertions.assertThat(webCheckpointFactory.build(state = state).isStarted).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isCompleted(state: WebCheckpoint.State, expected: Boolean) {
        Assertions.assertThat(webCheckpointFactory.build(state = state).isCompleted).isEqualTo(expected)
    }

    @MethodSource
    @ParameterizedTest
    fun isFailed(state: WebCheckpoint.State, expected: Boolean) {
        Assertions.assertThat(webCheckpointFactory.build(state = state).isFailed).isEqualTo(expected)
    }

    @Test
    fun transition_to_the_state_Started() {
        val now: LocalDateTime = now()

        val entity: WebCheckpoint = webCheckpointFactory.build()
        val actual: WebCheckpoint = entity.start()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebCheckpoint.State.Started)
            it.assertThat(actual.startedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Completed() {
        val snapshots: List<WebSnapshot> = listOf(webSnapshotFactory.build())
        val now: LocalDateTime = now()

        val entity: WebCheckpoint = webCheckpointFactory.build()
        val actual: WebCheckpoint = entity.complete(snapshots)

        SoftAssertions.assertSoftly {
            it.assertThat(actual.snapshots).isEqualTo(snapshots)
            it.assertThat(actual.state).isEqualTo(WebCheckpoint.State.Completed)
            it.assertThat(actual.completedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Failed() {
        val now: LocalDateTime = now()

        val entity: WebCheckpoint = webCheckpointFactory.build()
        val actual: WebCheckpoint = entity.fail()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebCheckpoint.State.Failed)
            it.assertThat(actual.failedDate).isAfterOrEqualTo(now)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    companion object {

        @JvmStatic
        fun isReady(): List<Arguments> {
            return listOf(
                Arguments.of(WebCheckpoint.State.Ready, true),
                Arguments.of(WebCheckpoint.State.Started, false),
                Arguments.of(WebCheckpoint.State.Completed, false),
                Arguments.of(WebCheckpoint.State.Failed, false),
            )
        }

        @JvmStatic
        fun isStarted(): List<Arguments> {
            return listOf(
                Arguments.of(WebCheckpoint.State.Ready, false),
                Arguments.of(WebCheckpoint.State.Started, true),
                Arguments.of(WebCheckpoint.State.Completed, false),
                Arguments.of(WebCheckpoint.State.Failed, false),
            )
        }

        @JvmStatic
        fun isCompleted(): List<Arguments> {
            return listOf(
                Arguments.of(WebCheckpoint.State.Ready, false),
                Arguments.of(WebCheckpoint.State.Started, false),
                Arguments.of(WebCheckpoint.State.Completed, true),
                Arguments.of(WebCheckpoint.State.Failed, false),
            )
        }

        @JvmStatic
        fun isFailed(): List<Arguments> {
            return listOf(
                Arguments.of(WebCheckpoint.State.Ready, false),
                Arguments.of(WebCheckpoint.State.Started, false),
                Arguments.of(WebCheckpoint.State.Completed, false),
                Arguments.of(WebCheckpoint.State.Failed, true),
            )
        }

    }

}
