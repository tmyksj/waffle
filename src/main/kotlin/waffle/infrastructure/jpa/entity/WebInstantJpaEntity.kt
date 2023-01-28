package waffle.infrastructure.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import waffle.domain.time.now
import java.time.LocalDateTime

/**
 * A set of checkpoints of pages.
 */
@Entity(name = "wf_web_instant")
@EntityListeners(AuditingEntityListener::class)
data class WebInstantJpaEntity(

    /**
     * ID.
     */
    @field:Id
    val id: String = "",

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
