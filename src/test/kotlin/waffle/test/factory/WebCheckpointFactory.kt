package waffle.test.factory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebFlowRepository
import waffle.domain.time.now
import java.time.LocalDateTime
import java.util.*

/**
 * Component for building a WebCheckpoint entity.
 */
@TestComponent
class WebCheckpointFactory {

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Autowired
    private lateinit var webSnapshotFactory: WebSnapshotFactory

    /**
     * Returns a new entity.
     */
    fun build(
        id: UUID = UUID.randomUUID(),
        flow: WebFlow = webFlowRepository.save(webFlowFactory.build()),
        snapshots: List<WebSnapshot> = listOf(webSnapshotFactory.build()),
        state: WebCheckpoint.State = WebCheckpoint.State.Ready,
        startedDate: LocalDateTime? = null,
        completedDate: LocalDateTime? = null,
        failedDate: LocalDateTime? = null,
        createdDate: LocalDateTime = now(),
        lastModifiedDate: LocalDateTime = createdDate,
    ): WebCheckpoint {
        return WebCheckpoint(
            id = id,
            flow = flow,
            snapshots = snapshots,
            state = state,
            startedDate = startedDate,
            completedDate = completedDate,
            failedDate = failedDate,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

    /**
     * Returns a modified entity based on a given entity.
     */
    fun modify(
        entity: WebCheckpoint,
        flow: WebFlow = entity.flow,
        snapshots: List<WebSnapshot> = entity.snapshots,
        state: WebCheckpoint.State = entity.state,
        startedDate: LocalDateTime? = entity.startedDate,
        completedDate: LocalDateTime? = entity.completedDate,
        failedDate: LocalDateTime? = entity.failedDate,
        createdDate: LocalDateTime = entity.createdDate,
        lastModifiedDate: LocalDateTime = now(),
    ): WebCheckpoint {
        return entity.copy(
            flow = flow,
            snapshots = snapshots,
            state = state,
            startedDate = startedDate,
            completedDate = completedDate,
            failedDate = failedDate,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

}
