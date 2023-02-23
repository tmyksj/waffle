package waffle.domain.repository

import waffle.domain.entity.WebFlow
import java.util.*

/**
 * Repository for [WebFlow].
 */
interface WebFlowRepository {

    /**
     * Retrieves an entity by its id.
     *
     * @param id
     * @return the entity with the given id or null if none found.
     */
    fun findById(id: UUID): WebFlow?

    /**
     * Saves a given entity.
     *
     * @param entity
     * @return the saved entity.
     */
    fun save(entity: WebFlow): WebFlow

}
