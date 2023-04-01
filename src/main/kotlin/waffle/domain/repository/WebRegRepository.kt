package waffle.domain.repository

import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import java.util.*

/**
 * Repository for [WebReg].
 */
interface WebRegRepository {

    /**
     * Retrieves entities by its checkpoint.
     *
     * @param checkpoint
     * @return the entities with the given checkpoint.
     */
    fun findAllByCheckpoint(checkpoint: WebCheckpoint): List<WebReg>

    /**
     * Retrieves an entity by its id.
     *
     * @param id
     * @return the entity with the given id or null if none found.
     */
    fun findById(id: UUID): WebReg?

    /**
     * Saves a given entity.
     *
     * @param entity
     * @return the saved entity.
     */
    fun save(entity: WebReg): WebReg

}
