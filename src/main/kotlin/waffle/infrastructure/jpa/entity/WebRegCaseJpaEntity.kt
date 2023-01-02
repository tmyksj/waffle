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
         * URL for expected page.
         */
        val expected: String = "",

        /**
         * URL for actual page.
         */
        val actual: String = "",

        ) {

    @Embeddable
    data class Id(

            @field:Column(name = "wf_web_reg_id")
            val webRegId: String = "",

            val id: Long = 0,

            ) : Serializable

}
