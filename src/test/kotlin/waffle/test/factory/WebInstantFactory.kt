package waffle.test.factory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import waffle.domain.entity.WebInstant
import waffle.domain.model.WebComposition
import waffle.domain.time.now
import java.time.LocalDateTime
import java.util.*

/**
 * Component for building a WebInstant entity.
 */
@TestComponent
class WebInstantFactory {

    @Autowired
    private lateinit var webCompositionFactory: WebCompositionFactory

    /**
     * Returns a new entity.
     */
    fun build(
        id: UUID = UUID.randomUUID(),
        compositions: List<WebComposition> = listOf(webCompositionFactory.build()),
        createdDate: LocalDateTime = now(),
        lastModifiedDate: LocalDateTime = createdDate,
    ): WebInstant {
        return WebInstant(
            id = id,
            compositions = compositions,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

    /**
     * Returns a modified entity based on a given entity.
     */
    fun modify(
        entity: WebInstant,
        compositions: List<WebComposition> = entity.compositions,
        createdDate: LocalDateTime = entity.createdDate,
        lastModifiedDate: LocalDateTime = now(),
    ): WebInstant {
        return entity.copy(
            compositions = compositions,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

}
