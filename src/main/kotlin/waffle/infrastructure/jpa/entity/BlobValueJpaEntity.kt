package waffle.infrastructure.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * Bytes of a binary large object.
 */
@Entity(name = "wf_blob_value")
@EntityListeners(AuditingEntityListener::class)
class BlobValueJpaEntity(

    /**
     * ID.
     */
    @field:Id
    val id: String = "",

    /**
     * Bytes.
     */
    val value: ByteArray = ByteArray(0),

    )
