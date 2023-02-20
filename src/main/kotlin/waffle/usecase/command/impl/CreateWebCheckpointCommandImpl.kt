package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.usecase.command.CreateWebCheckpointCommand
import java.util.*

@Component
@Transactional
class CreateWebCheckpointCommandImpl(
    private val webCheckpointRepository: WebCheckpointRepository,
    private val webFlowRepository: WebFlowRepository,
) : CreateWebCheckpointCommand {

    override fun execute(
        flowId: UUID,
    ): CreateWebCheckpointCommand.Response {
        val flow: WebFlow = webFlowRepository.findById(flowId)
            ?: return CreateWebCheckpointCommand.Response.Error(
                isNotFound = true,
            )

        val entity: WebCheckpoint = webCheckpointRepository.save(
            WebCheckpoint(
                flow = flow,
            ),
        )

        return CreateWebCheckpointCommand.Response.Ok(
            webCheckpoint = entity,
        )
    }

}
