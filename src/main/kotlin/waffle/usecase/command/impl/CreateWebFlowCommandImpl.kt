package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebFlowRepository
import waffle.usecase.command.CreateWebFlowCommand

@Component
@Transactional
class CreateWebFlowCommandImpl(
    private val webFlowRepository: WebFlowRepository,
) : CreateWebFlowCommand {

    override fun execute(
        compositions: List<WebComposition>,
    ): CreateWebFlowCommand.Response {
        val entity: WebFlow = webFlowRepository.save(
            WebFlow(
                compositions = compositions,
            ),
        )

        return CreateWebFlowCommand.Response.Ok(
            webFlow = entity,
        )
    }

}
