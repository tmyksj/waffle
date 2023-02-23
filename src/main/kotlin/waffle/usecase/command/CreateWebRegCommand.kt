package waffle.usecase.command

import waffle.domain.entity.WebReg
import java.net.URL

/**
 * Creates a [WebReg] entity.
 */
interface CreateWebRegCommand {

    /**
     * Executes a command with a given arguments.
     *
     * @param webRegCases
     * @return
     */
    fun execute(
        webRegCases: List<WebRegCase>,
    ): Response

    /**
     * Test case.
     */
    data class WebRegCase(

        /**
         * Composition for an expected page.
         */
        val expected: WebRegComposition,

        /**
         * Composition for an actual page.
         */
        val actual: WebRegComposition,

        )

    /**
     * Composition for a page.
     */
    data class WebRegComposition(

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
