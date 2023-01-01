package waffle.infrastructure.jpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import waffle.domain.time.now
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
         * Test result.
         */
        val result: ByteArray? = null,

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

        ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebRegJpaEntity

        if (id != other.id) return false
        if (result != null) {
            if (other.result == null) return false
            if (!result.contentEquals(other.result)) return false
        } else if (other.result != null) return false
        if (state != other.state) return false
        if (startedDate != other.startedDate) return false
        if (completedDate != other.completedDate) return false
        if (failedDate != other.failedDate) return false
        if (createdDate != other.createdDate) return false
        if (lastModifiedDate != other.lastModifiedDate) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = id.hashCode()
        result1 = 31 * result1 + (result?.contentHashCode() ?: 0)
        result1 = 31 * result1 + state.hashCode()
        result1 = 31 * result1 + (startedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + (completedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + (failedDate?.hashCode() ?: 0)
        result1 = 31 * result1 + createdDate.hashCode()
        result1 = 31 * result1 + lastModifiedDate.hashCode()
        result1 = 31 * result1 + (version?.hashCode() ?: 0)
        return result1
    }
}
