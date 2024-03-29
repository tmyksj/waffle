package waffle.usecase.command

import waffle.domain.entity.WebCheckpoint
import java.util.*

/**
 * Creates a [WebCheckpoint] entity.
 */
interface CreateWebCheckpointCommand {

    /**
     * Executes a command with given arguments.
     *
     * @param flow
     * @return
     */
    fun execute(
        flow: UUID,
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
             * true if the WebFlow is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
