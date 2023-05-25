package waffle.domain.entity

import waffle.core.time.now
import waffle.domain.model.WebComposition
import java.time.LocalDateTime
import java.util.*

/**
 * Instants of pages.
 */
data class WebFlow(

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

    ) {

    /**
     * Recompose compositions.
     *
     * @param compositions
     * @return
     */
    fun recompose(compositions: List<WebComposition>): WebFlow {
        return copy(
            compositions = compositions,
            lastModifiedDate = now(),
        )
    }

}
