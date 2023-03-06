package waffle.usecase.command

import waffle.domain.entity.WebReg
import java.net.URL

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

    }

}
