package waffle.usecase.query

import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import java.util.*

/**
 * Finds a [WebCheckpoint].
 */
interface FindWebCheckpointQuery {

    /**
     * Executes a query with given arguments.
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
            val webCheckpoint: WebCheckpoint,

            /**
             * List of WebReg.
             */
            val regs: List<WebReg>,

            ) : Response

        data class Error(

            /**
             * true if the WebCheckpoint is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
