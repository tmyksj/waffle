package waffle.infrastructure.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import waffle.core.time.now
import java.time.LocalDateTime

/**
 * Binary large object.
 */
@Entity(name = "wf_blob")
@EntityListeners(AuditingEntityListener::class)
class BlobJpaEntity(

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
