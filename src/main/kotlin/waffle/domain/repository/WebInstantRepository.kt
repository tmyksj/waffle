package waffle.domain.repository

import waffle.domain.entity.WebInstant
import java.util.*

/**
 * Repository for WebInstant.
 */
interface WebInstantRepository {

    /**
     * Retrieves an entity by its id.
     * @param id
     * @return the entity with the given id or null if none found.
     */
    fun findById(id: UUID): WebInstant?

    /**
     * Saves a given entity.
     * @param entity
     * @return the saved entity.
     */
    fun save(entity: WebInstant): WebInstant

}
