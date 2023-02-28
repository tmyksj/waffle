package waffle.infrastructure.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * Bytes of a binary large object.
 */
@Entity(name = "wf_blob_value")
@EntityListeners(AuditingEntityListener::class)
data class BlobValueJpaEntity(

    /**
     * ID.
     */
    @field:Column(name = "wf_blob_id")
    @field:Id
    val blobId: String = "",

    /**
     * Bytes.
     */
    val value: ByteArray = ByteArray(0),

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlobValueJpaEntity

        if (blobId != other.blobId) return false
        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = blobId.hashCode()
        result = 31 * result + value.contentHashCode()
        return result
    }

}
