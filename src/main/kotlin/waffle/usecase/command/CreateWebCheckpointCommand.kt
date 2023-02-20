package waffle.usecase.command

import waffle.domain.entity.WebCheckpoint
import java.util.*

/**
 * Creates a WebCheckpoint entity.
 */
interface CreateWebCheckpointCommand {

    /**
     * Executes a command with a given arguments.
     *
     * @param flowId
     * @return
     */
    fun execute(
        flowId: UUID,
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
