package waffle.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import waffle.domain.time.now
import java.time.LocalDateTime

/**
 * Checkpoint of pages.
 */
@Entity(name = "wf_web_checkpoint")
@EntityListeners(AuditingEntityListener::class)
data class WebCheckpointJpaEntity(

    /**
     * ID.
     */
    @field:Id
    val id: String = "",

    /**
     * WebFlow.
     */
    @field:Column(name = "wf_web_flow_id")
    val webFlowId: String = "",

    /**
     * State.
     */
    val state: Long = 0,

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

    /**
     * Version to implement optimistic locking.
     */
    @field:Version
    val version: Long? = null,

    )
