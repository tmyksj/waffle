package waffle.domain.entity

import waffle.core.time.now
import java.net.URL
import java.time.LocalDateTime
import java.util.*

/**
 * Regression test for Web.
 */
data class WebReg(

    /**
     * ID.
     */
    val id: UUID = UUID.randomUUID(),

    /**
     * Test cases.
     */
    val cases: List<Case> = listOf(),

    /**
     * Test result.
     */
    val result: ByteArray? = null,

    /**
     * State.
     */
    val state: State = State.Ready,

    /**
     * Date that the test was started.
     */
    val startedDate: LocalDateTime? = null,

    /**
     * Date that the test was completed.
     */
    val completedDate: LocalDateTime? = null,

    /**
     * Date that the test was failed.
     */
    val failedDate: LocalDateTime? = null,

    /**
     * Date that the entity was created.
     */
    val createdDate: LocalDateTime = now(),

    /**
     * Date that the entity was recently modified.
     */
    val lastModifiedDate: LocalDateTime = createdDate,

    ) {

    /**
     * The transition to the state "Started".
     *
     * @return
     */
    fun start(): WebReg {
        return copy(
            state = State.Started,
            startedDate = now(),
            lastModifiedDate = now(),
        )
    }

    /**
     * The transition to the state "Completed" with a given result.
     *
     * @param result
     * @return
     */
    fun complete(result: ByteArray): WebReg {
        return copy(
            result = result,
            state = State.Completed,
            completedDate = now(),
            lastModifiedDate = now(),
        )
    }

    /**
     * The transition to the state "Failed".
     *
     * @return
     */
    fun fail(): WebReg {
        return copy(
            state = State.Failed,
            failedDate = now(),
            lastModifiedDate = now(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebReg

        if (id != other.id) return false
        if (cases != other.cases) return false
        if (result != null) {
            if (other.result == null) return false
            if (!result.contentEquals(other.result)) return false
        } else if (other.result != null) return false
        if (state != other.state) return false
        if (startedDate != other.startedDate) return false
        if (completedDate != other.completedDate) return false
        if (failedDate != other.failedDate) return false
        if (createdDate != other.createdDate) return false
        if (lastModifiedDate != other.lastModifiedDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = id.hashCode()
        result1 = 31 * result1 + cases.hashCode()
        result1 = 31 * result1 + (result?.contentHashCode() ?: 0)
        result1 = 31 * result1 + state.hashCode()
        result1 = 31 * result1 + (startedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + (completedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + (failedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + createdDate.hashCode()
        result1 = 31 * result1 + lastModifiedDate.hashCode()
        return result1
    }

    /**
     * Test case.
     */
    data class Case(

        /**
         * Composition for an expected page.
         */
        val expected: Composition,

        /**
         * Composition for an actual page.
         */
        val actual: Composition,

        )

    /**
     * Composition for a page.
     */
    data class Composition(

        /**
         * URL for a page.
         */
        val resource: URL,

        /**
         * Window width for a page (pixels).
         */
        val widthPx: Long = 1920,

        /**
         * Waiting time before accessing to a page (milliseconds).
         */
        val delayMs: Long = 0,

        ) {

        init {
            // widthPx must be in range from 100 to 4000.
            if (widthPx !in 100..4000) {
                throw IllegalArgumentException()
            }

            // delayMs must be in range from zero to 1 minute.
            if (delayMs !in 0..60000) {
                throw IllegalArgumentException()
            }
        }

    }

    /**
     * WebReg state.
     */
    enum class State {

        /**
         * The test is ready.
         */
        Ready,

        /**
         * The test was started.
         */
        Started,

        /**
         * The test was completed.
         */
        Completed,

        /**
         * The test was failed.
         */
        Failed,

    }

}
