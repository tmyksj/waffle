package waffle.usecase.command

import waffle.domain.entity.WebFlow
import java.net.URL

/**
 * Creates a [WebFlow] entity.
 */
interface CreateWebFlowCommand {

    /**
     * Executes a command with given arguments.
     *
     * @param compositions
     * @return
     */
    fun execute(
        compositions: List<WebComposition>,
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
             * WebFlow.
             */
            val webFlow: WebFlow,

            ) : Response

    }

}
