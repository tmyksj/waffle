package waffle.domain.repository

import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import java.util.*

/**
 * Repository for [WebCheckpoint].
 */
interface WebCheckpointRepository {

    /**
     * Retrieves entities by its flow.
     *
     * @param flow
     * @return the entities with the given flow.
     */
    fun findAllByFlow(flow: WebFlow): List<WebCheckpoint>

    /**
     * Retrieves entities by its id.
     *
     * @param ids
     * @return the entities with the given ids.
     */
    fun findAllById(ids: List<UUID>): List<WebCheckpoint>

    /**
     * Retrieves an entity by its id.
     *
     * @param id
     * @return the entity with the given id or null if none found.
     */
    fun findById(id: UUID): WebCheckpoint?

    /**
     * Saves a given entity.
     *
     * @param entity
     * @return the saved entity.
     */
    fun save(entity: WebCheckpoint): WebCheckpoint

}
