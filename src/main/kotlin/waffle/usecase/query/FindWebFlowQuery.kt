package waffle.usecase.query

import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import java.util.*

/**
 * Finds a [WebFlow].
 */
interface FindWebFlowQuery {

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
             * WebFlow.
             */
            val webFlow: WebFlow,

            /**
             * List of WebCheckpoint in descending order of max of startedDate, completedDate, failedDate and
             * createdDate.
             */
            val checkpoints: List<WebCheckpoint>,

            ) : Response

        data class Error(

            /**
             * true if the WebFlow is not found.
             */
            val isNotFound: Boolean = false,

            ) : Response

    }

}
