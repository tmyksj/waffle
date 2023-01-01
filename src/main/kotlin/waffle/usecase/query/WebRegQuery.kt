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
     * Returns result.
     * @param id
     * @return
     */
    fun result(
            id: UUID,
    ): ResultResponse

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

    /**
     * Response for result.
     */
    interface ResultResponse {

        data class Ok(

                val result: ByteArray,

                ) : ResultResponse {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Ok

                if (!result.contentEquals(other.result)) return false

                return true
            }

            override fun hashCode(): Int {
                return result.contentHashCode()
            }

        }

        data class Error(

                /**
                 * True if WebReg is not found.
                 */
                val isNotFound: Boolean = false,

                ) : ResultResponse

    }

}
