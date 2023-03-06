package waffle.test.factory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import waffle.core.time.now
import waffle.core.type.Blob
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebCheckpointRepository
import java.time.LocalDateTime
import java.util.*

/**
 * Component for building a WebReg entity.
 */
@TestComponent
class WebRegFactory {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    /**
     * Returns a new entity.
     */
    fun build(
        id: UUID = UUID.randomUUID(),
        checkpointA: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build()),
        checkpointB: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build()),
        output: Blob? = null,
        state: WebReg.State = WebReg.State.Ready,
        startedDate: LocalDateTime? = null,
        completedDate: LocalDateTime? = null,
        failedDate: LocalDateTime? = null,
        createdDate: LocalDateTime = now(),
        lastModifiedDate: LocalDateTime = createdDate,
    ): WebReg {
        return WebReg(
            id = id,
            checkpointA = checkpointA,
            checkpointB = checkpointB,
            output = output,
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
        entity: WebReg,
        checkpointA: WebCheckpoint = entity.checkpointA,
        checkpointB: WebCheckpoint = entity.checkpointB,
        output: Blob? = entity.output,
        state: WebReg.State = entity.state,
        startedDate: LocalDateTime? = entity.startedDate,
        completedDate: LocalDateTime? = entity.completedDate,
        failedDate: LocalDateTime? = entity.failedDate,
        createdDate: LocalDateTime = entity.createdDate,
        lastModifiedDate: LocalDateTime = now(),
    ): WebReg {
        return entity.copy(
            checkpointA = checkpointA,
            checkpointB = checkpointB,
            output = output,
            state = state,
            startedDate = startedDate,
            completedDate = completedDate,
            failedDate = failedDate,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

}
