package waffle.domain.entity

import waffle.domain.model.WebComposition
import waffle.domain.time.now
import java.time.LocalDateTime
import java.util.*

/**
 * A set of checkpoints of pages.
 */
data class WebInstant(

    /**
     * ID.
     */
    val id: UUID = UUID.randomUUID(),

    /**
     * Compositions for creating a checkpoint.
     */
    val compositions: List<WebComposition> = listOf(),

    /**
     * Date that the entity was created.
     */
    val createdDate: LocalDateTime = now(),

    /**
     * Date that the entity was recently modified.
     */
    val lastModifiedDate: LocalDateTime = createdDate,

    )
