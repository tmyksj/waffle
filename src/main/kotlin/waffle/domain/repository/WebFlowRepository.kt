package waffle.domain.repository

import waffle.domain.entity.WebFlow
import java.util.*

/**
 * Repository for [WebFlow].
 */
interface WebFlowRepository {

    /**
     * Retrieves entities by its id.
     *
     * @param ids
     * @return the entities with the given ids.
     */
    fun findAllById(ids: List<UUID>): List<WebFlow>

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
