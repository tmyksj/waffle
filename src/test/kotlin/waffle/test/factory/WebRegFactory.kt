package waffle.test.factory

import org.springframework.boot.test.context.TestComponent
import waffle.domain.entity.WebReg
import waffle.domain.time.now
import java.net.URL
import java.time.LocalDateTime
import java.util.*

/**
 * Component for building a WebReg entity.
 */
@TestComponent
class WebRegFactory {

    /**
     * Returns a new entity.
     */
    fun build(
        id: UUID = UUID.randomUUID(),
        cases: List<WebReg.Case> = listOf(
            WebReg.Case(
                expected = URL("http://127.0.0.1:8081"),
                actual = URL("http://127.0.0.1:8081"),
            ),
        ),
        result: ByteArray? = null,
        state: WebReg.State = WebReg.State.Ready,
        startedDate: LocalDateTime? = null,
        completedDate: LocalDateTime? = null,
        failedDate: LocalDateTime? = null,
        createdDate: LocalDateTime = now(),
        lastModifiedDate: LocalDateTime = createdDate,
    ): WebReg {
        return WebReg(
            id = id,
            cases = cases,
            result = result,
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
        cases: List<WebReg.Case> = entity.cases,
        result: ByteArray? = entity.result,
        state: WebReg.State = entity.state,
        startedDate: LocalDateTime? = entity.startedDate,
        completedDate: LocalDateTime? = entity.completedDate,
        failedDate: LocalDateTime? = entity.failedDate,
        createdDate: LocalDateTime = entity.createdDate,
        lastModifiedDate: LocalDateTime = now(),
    ): WebReg {
        return entity.copy(
            cases = cases,
            result = result,
            state = state,
            startedDate = startedDate,
            completedDate = completedDate,
            failedDate = failedDate,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

}
