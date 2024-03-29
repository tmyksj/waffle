package waffle.usecase.query

import waffle.domain.entity.WebReg
import java.util.*

/**
 * Finds a [WebReg].
 */
interface FindWebRegQuery {

    /**
     * Executes a query with given arguments.
     *
     * @param id
     * @return
     */
    fun execute(
        id: UUID,
    ): Response

    interface Response {

        data class Ok(

            /**
             * WebReg.
             */
            val webReg: WebReg,

            ) : Response

        data class Error(

            /**
             * true if WebReg is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
