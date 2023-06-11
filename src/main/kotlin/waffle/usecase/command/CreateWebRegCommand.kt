package waffle.usecase.command

import waffle.domain.entity.WebReg
import java.net.URL
import java.util.*

/**
 * Creates a [WebReg] entity.
 */
interface CreateWebRegCommand {

    /**
     * Executes a command with given arguments.
     *
     * @param checkpointA
     * @param checkpointB
     * @return
     */
    fun execute(
        checkpointA: UUID,
        checkpointB: UUID,
    ): Response

    /**
     * Executes a command with given arguments.
     *
     * @param checkpointA
     * @param checkpointB
     * @return
     */
    fun execute(
        checkpointA: List<WebComposition>,
        checkpointB: List<WebComposition>,
    ): Response

    /**
     * Composition for a page.
     */
    data class WebComposition(

        /**
         * URL for a page.
         */
        val resource: URL,

        /**
         * Window width for a page (pixels).
         */
        val widthPx: Long,

        /**
         * Window height for a page (pixels).
         */
        val heightPx: Long,

        /**
         * Waiting time before accessing to a page (milliseconds).
         */
        val delayMs: Long,

        )

    interface Response {

        data class Ok(

            /**
             * WebReg.
             */
            val webReg: WebReg,

            ) : Response

        data class Error(

            /**
             * true if the WebCheckpoint A is not found.
             */
            val isNotFoundCheckpointA: Boolean = false,

            /**
             * true if the WebCheckpoint B is not found.
             */
            val isNotFoundCheckpointB: Boolean = false,

            ) : Response

    }

}
