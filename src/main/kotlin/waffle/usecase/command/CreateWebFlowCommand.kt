package waffle.usecase.command

import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition

/**
 * Creates a WebFlow entity.
 */
interface CreateWebFlowCommand {

    /**
     * Executes a command with a given arguments.
     * @param compositions
     * @return
     */
    fun execute(
        compositions: List<WebComposition>,
    ): Response

    interface Response {

        data class Ok(

            /**
             * WebFlow.
             */
            val webFlow: WebFlow,

            ) : Response

    }

}
