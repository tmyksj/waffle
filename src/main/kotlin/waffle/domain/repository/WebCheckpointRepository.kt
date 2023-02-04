package waffle.domain.repository

import waffle.domain.entity.WebCheckpoint
import java.util.*

/**
 * Repository for WebCheckpoint.
 */
interface WebCheckpointRepository {

    /**
     * Retrieves an entity by its id.
     * @param id
     * @return the entity with the given id or null if none found.
     */
    fun findById(id: UUID): WebCheckpoint?

    /**
     * Saves a given entity.
     * @param entity
     * @return the saved entity.
     */
    fun save(entity: WebCheckpoint): WebCheckpoint

}
