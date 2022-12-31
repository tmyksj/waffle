package waffle.usecase.query

import waffle.domain.entity.WebReg
import java.util.*

/**
 * Queries for WebReg.
 */
interface WebRegQuery {

    /**
     * Returns details.
     * @param id
     * @return
     */
    fun details(
            id: UUID,
    ): DetailsResponse

    /**
     * Response for details.
     */
    interface DetailsResponse {

        data class Ok(

                /**
                 * WebReg.
                 */
                val webReg: WebReg,

                ) : DetailsResponse

        data class Error(

                /**
                 * True if WebReg is not found.
                 */
                val isNotFound: Boolean = false,

                ) : DetailsResponse

    }

}
