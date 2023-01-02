package waffle.domain.entity

import waffle.domain.time.now
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
     * @return
     */
    fun start(): WebReg {
        return copy(
            state = State.Started,
            lastModifiedDate = now(),
        )
    }

    /**
     * The transition to the state "Completed" with a given result.
     * @param result
     * @return
     */
    fun complete(result: ByteArray): WebReg {
        return copy(
            result = result,
            state = State.Completed,
            lastModifiedDate = now(),
        )
    }

    /**
     * The transition to the state "Failed".
     * @return
     */
    fun fail(): WebReg {
        return copy(
            state = State.Failed,
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
         * URL for expected page.
         */
        val expected: URL,

        /**
         * URL for actual page.
         */
        val actual: URL,

        )

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
