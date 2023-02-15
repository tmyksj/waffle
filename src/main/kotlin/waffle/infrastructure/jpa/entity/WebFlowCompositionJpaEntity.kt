package waffle.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

/**
 * Composition for creating a checkpoint.
 */
@Entity(name = "wf_web_flow_composition")
@EntityListeners(AuditingEntityListener::class)
data class WebFlowCompositionJpaEntity(

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
     * Waiting time before accessing to a page (milliseconds).
     */
    val delayMs: Long = 0,

    ) {

    @Embeddable
    data class Id(

        @field:Column(name = "wf_web_flow_id")
        val webFlowId: String = "",

        val idx: Long = 0,

        ) : Serializable

}
