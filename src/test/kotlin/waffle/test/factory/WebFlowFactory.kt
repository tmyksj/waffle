package waffle.test.factory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition
import waffle.domain.time.now
import java.time.LocalDateTime
import java.util.*

/**
 * Component for building a WebFlow entity.
 */
@TestComponent
class WebFlowFactory {

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
    ): WebFlow {
        return WebFlow(
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
        entity: WebFlow,
        compositions: List<WebComposition> = entity.compositions,
        createdDate: LocalDateTime = entity.createdDate,
        lastModifiedDate: LocalDateTime = now(),
    ): WebFlow {
        return entity.copy(
            compositions = compositions,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate,
        )
    }

}
