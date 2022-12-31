package waffle.usecase.command

import waffle.domain.entity.WebReg
import java.net.URL

/**
 * Commands for WebReg.
 */
interface WebRegCommand {

    /**
     * Creates an entity.
     * @param webRegCases
     * @return
     */
    fun create(
            webRegCases: List<WebRegCase>,
    ): CreateResponse

    /**
     * Response for create.
     */
    interface CreateResponse {

        data class Ok(

                /**
                 * WebReg.
                 */
                val webReg: WebReg,

                ) : CreateResponse

    }

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

}
