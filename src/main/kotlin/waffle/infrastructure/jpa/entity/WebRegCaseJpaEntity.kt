package waffle.infrastructure.jpa.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable

/**
 * Regression test case for Web.
 */
@Entity(name = "wf_web_reg_case")
@EntityListeners(AuditingEntityListener::class)
data class WebRegCaseJpaEntity(

    /**
     * ID.
     */
    @field:EmbeddedId
    val id: Id = Id(),

    /**
     * URL for an expected page.
     */
    val expectedResource: String = "",

    /**
     * Waiting time before accessing to an expected page (milliseconds).
     */
    val expectedDelayMs: Long = 0,

    /**
     * URL for an actual page.
     */
    val actualResource: String = "",

    /**
     * Waiting time before accessing to an actual page (milliseconds).
     */
    val actualDelayMs: Long = 0,

    ) {

    @Embeddable
    data class Id(

        @field:Column(name = "wf_web_reg_id")
        val webRegId: String = "",

        val id: Long = 0,

        ) : Serializable

}
