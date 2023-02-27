package waffle.domain.entity

import waffle.core.time.now
import waffle.domain.model.WebSnapshot
import java.time.LocalDateTime
import java.util.*

/**
 * Checkpoint of pages.
 */
data class WebCheckpoint(

    /**
     * ID.
     */
    val id: UUID = UUID.randomUUID(),

    /**
     * WebFlow.
     */
    val flow: WebFlow,

    /**
     * Snapshots.
     */
    val snapshots: List<WebSnapshot> = listOf(),

    /**
     * State.
     */
    val state: State = State.Ready,

    /**
     * Date that the creation was started.
     */
    val startedDate: LocalDateTime? = null,

    /**
     * Date that the creation was completed.
     */
    val completedDate: LocalDateTime? = null,

    /**
     * Date that the creation was failed.
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
    fun start(): WebCheckpoint {
        return copy(
            state = State.Started,
            startedDate = now(),
            lastModifiedDate = now(),
        )
    }

    /**
     * The transition to the state "Completed" with a given snapshots.
     *
     * @param snapshots
     * @return
     */
    fun complete(snapshots: List<WebSnapshot>): WebCheckpoint {
        return copy(
            snapshots = snapshots,
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
    fun fail(): WebCheckpoint {
        return copy(
            state = State.Failed,
            failedDate = now(),
            lastModifiedDate = now(),
        )
    }

    /**
     * WebCheckpoint state.
     */
    enum class State {

        /**
         * The creation is ready.
         */
        Ready,

        /**
         * The creation was started.
         */
        Started,

        /**
         * The creation was completed.
         */
        Completed,

        /**
         * The creation was failed.
         */
        Failed,

    }

}
