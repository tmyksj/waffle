package waffle.usecase.command

import waffle.domain.entity.WebFlow
import java.net.URL
import java.util.*

/**
 * Modifies a [WebFlow] entity.
 */
interface ModifyWebFlowCommand {

    /**
     * Executes a command with given arguments.
     *
     * @param id
     * @param compositions
     * @return
     */
    fun execute(
        id: UUID,
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

        data class Error(

            /**
             * true if the WebFlow is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
