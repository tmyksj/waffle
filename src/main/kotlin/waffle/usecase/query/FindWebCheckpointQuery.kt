package waffle.usecase.query

import waffle.domain.entity.WebCheckpoint
import java.util.*

/**
 * Finds a [WebCheckpoint].
 */
interface FindWebCheckpointQuery {

    /**
     * Executes a query with a given arguments.
     *
     * @param id
     * @return
     */
    fun execute(
        id: UUID,
    ): Response

    interface Response {

        data class Ok(

            /**
             * WebCheckpoint.
             */
            val webCheckpoint: WebCheckpoint

        ) : Response

        data class Error(

            /**
             * true if the WebCheckpoint is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
