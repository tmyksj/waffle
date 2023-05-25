package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebFlowRepository
import waffle.usecase.command.ModifyWebFlowCommand
import java.util.*

@Component
@Transactional
class ModifyWebFlowCommandImpl(
    private val webFlowRepository: WebFlowRepository,
) : ModifyWebFlowCommand {

    override fun execute(
        id: UUID,
        compositions: List<ModifyWebFlowCommand.WebComposition>,
    ): ModifyWebFlowCommand.Response {
        val entity: WebFlow = webFlowRepository.findById(id)
            ?: return ModifyWebFlowCommand.Response.Error(
                isNotFound = true,
            )

        val modifiedEntity: WebFlow = webFlowRepository.save(
            entity.recompose(
                compositions.map {
                    WebComposition(
                        resource = it.resource,
                        widthPx = it.widthPx,
                        delayMs = it.delayMs,
                    )
                },
            )
        )

        return ModifyWebFlowCommand.Response.Ok(
            webFlow = modifiedEntity,
        )
    }

}
