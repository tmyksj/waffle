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
     * @param webFlowCompositions
     * @return
     */
    fun execute(
        webFlowCompositions: List<WebFlowComposition>,
    ): Response

    /**
     * Composition for a page.
     */
    data class WebFlowComposition(

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
