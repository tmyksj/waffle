package waffle.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import waffle.core.time.now
import java.time.LocalDateTime
import java.util.*

/**
 * Regression test for Web.
 */
@Entity(name = "wf_web_reg")
@EntityListeners(AuditingEntityListener::class)
data class WebRegJpaEntity(

    /**
     * ID.
     */
    @field:Id
    val id: String = "",

    /**
     * WebCheckpoint A.
     */
    @field:Column(name = "wf_web_checkpoint_id_a")
    val webCheckpointIdA: String = "",

    /**
     * WebCheckpoint B.
     */
    @field:Column(name = "wf_web_checkpoint_id_b")
    val webCheckpointIdB: String = "",

    /**
     * Output.
     */
    val output: String? = null,

    /**
     * State.
     */
    val state: Long = 0,

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

    /**
     * Version to implement optimistic locking.
     */
    @field:Version
    val version: Long? = null,

    )
