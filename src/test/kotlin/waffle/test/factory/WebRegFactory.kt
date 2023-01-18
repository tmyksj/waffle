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
        cases: List<WebReg.Case> = listOf(buildCase()),
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
     * Returns a new entity.
     */
    fun buildCase(
        expected: WebReg.Composition = buildComposition(),
        actual: WebReg.Composition = buildComposition(),
    ): WebReg.Case {
        return WebReg.Case(
            expected = expected,
            actual = actual,
        )
    }

    /**
     * Returns a new entity.
     */
    fun buildComposition(
        resource: URL = URL("http://127.0.0.1:8081"),
        widthPx: Long = 1920,
        delayMs: Long = 0,
    ): WebReg.Composition {
        return WebReg.Composition(
            resource = resource,
            widthPx = widthPx,
            delayMs = delayMs,
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
