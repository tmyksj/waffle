package waffle.domain.service

import waffle.domain.entity.WebCheckpoint

/**
 * Service for [WebCheckpoint].
 */
interface WebCheckpointService {

    /**
     * Creates or recreates a checkpoint.
     *
     * @param entity
     */
    fun create(entity: WebCheckpoint)

}
