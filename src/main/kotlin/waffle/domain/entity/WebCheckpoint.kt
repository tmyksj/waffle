package waffle.domain.entity

import waffle.domain.model.WebSnapshot
import waffle.domain.time.now
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
