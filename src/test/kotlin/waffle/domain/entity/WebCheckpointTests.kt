package waffle.domain.entity

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
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

}
