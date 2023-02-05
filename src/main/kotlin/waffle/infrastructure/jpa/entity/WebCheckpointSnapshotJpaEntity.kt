package waffle.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

/**
 * Snapshot of a page.
 */
@Entity(name = "wf_web_checkpoint_snapshot")
@EntityListeners(AuditingEntityListener::class)
data class WebCheckpointSnapshotJpaEntity(

    /**
     * ID.
     */
    @field:EmbeddedId
    val id: Id = Id(),

    /**
     * URL for a page.
     */
    val resource: String = "",

    /**
     * Window width for a page (pixels).
     */
    val widthPx: Long = 1920,

    /**
     * Screenshot of a page.
     */
    val screenshot: ByteArray = ByteArray(0),

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebCheckpointSnapshotJpaEntity

        if (id != other.id) return false
        if (resource != other.resource) return false
        if (widthPx != other.widthPx) return false
        if (!screenshot.contentEquals(other.screenshot)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + resource.hashCode()
        result = 31 * result + widthPx.hashCode()
        result = 31 * result + screenshot.contentHashCode()
        return result
    }

    @Embeddable
    data class Id(

        @field:Column(name = "wf_web_checkpoint_id")
        val webCheckpointId: String = "",

        val id: Long = 0,

        ) : Serializable

}
