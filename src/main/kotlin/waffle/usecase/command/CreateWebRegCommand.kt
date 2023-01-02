package waffle.usecase.command

import waffle.domain.entity.WebReg
import java.net.URL

/**
 * Creates a WebReg entity.
 */
interface CreateWebRegCommand {

    /**
     * Executes a command with a given arguments.
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
         * URL for expected page.
         */
        val expected: URL,

        /**
         * URL for actual page.
         */
        val actual: URL,

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
