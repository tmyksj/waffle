package waffle.domain.entity

import waffle.core.time.now
import waffle.core.type.Blob
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
     * WebCheckpoint A.
     * Generally used as an expected value.
     */
    val checkpointA: WebCheckpoint,

    /**
     * WebCheckpoint B.
     * Generally used as an actual value.
     */
    val checkpointB: WebCheckpoint,

    /**
     * Test result.
     */
    val result: Blob? = null,

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
     * Whether this is ready.
     */
    val isReady: Boolean
        get() {
            return state == State.Ready
        }

    /**
     * Whether this is started.
     */
    val isStarted: Boolean
        get() {
            return state == State.Started
        }

    /**
     * Whether this is completed.
     */
    val isCompleted: Boolean
        get() {
            return state == State.Completed
        }

    /**
     * Whether this is failed.
     */
    val isFailed: Boolean
        get() {
            return state == State.Failed
        }

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
    fun complete(result: Blob): WebReg {
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
