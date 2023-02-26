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
    val screenshot: String = "",

    ) {

    @Embeddable
    data class Id(

        @field:Column(name = "wf_web_checkpoint_id")
        val webCheckpointId: String = "",

        val idx: Long = 0,

        ) : Serializable

}
