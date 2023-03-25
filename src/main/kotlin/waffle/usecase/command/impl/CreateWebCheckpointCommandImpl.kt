package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
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
        flow: UUID,
    ): CreateWebCheckpointCommand.Response {
        val entity: WebCheckpoint = webCheckpointRepository.save(
            WebCheckpoint(
                flow = webFlowRepository.findById(flow)
                    ?: return CreateWebCheckpointCommand.Response.Error(
                        isNotFound = true,
                    ),
            ),
        )

        return CreateWebCheckpointCommand.Response.Ok(
            webCheckpoint = entity,
        )
    }

}
