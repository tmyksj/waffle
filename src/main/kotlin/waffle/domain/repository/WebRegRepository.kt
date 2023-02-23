package waffle.domain.repository

import waffle.domain.entity.WebReg
import java.util.*

/**
 * Repository for [WebReg].
 */
interface WebRegRepository {

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
