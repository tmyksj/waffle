package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.entity.WebReg
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.domain.repository.WebRegRepository
import waffle.usecase.command.CreateWebRegCommand

@Component
@Transactional
class CreateWebRegCommandImpl(
    private val webCheckpointRepository: WebCheckpointRepository,
    private val webFlowRepository: WebFlowRepository,
    private val webRegRepository: WebRegRepository,
) : CreateWebRegCommand {

    override fun execute(
        checkpointA: List<CreateWebRegCommand.WebComposition>,
        checkpointB: List<CreateWebRegCommand.WebComposition>,
    ): CreateWebRegCommand.Response {
        val entity: WebReg = webRegRepository.save(
            WebReg(
                checkpointA = webCheckpointRepository.save(
                    WebCheckpoint(
                        flow = webFlowRepository.save(
                            WebFlow(
                                compositions = checkpointA.map {
                                    WebComposition(
                                        resource = it.resource,
                                        widthPx = it.widthPx,
                                        delayMs = it.delayMs,
                                    )
                                },
                            ),
                        ),
                    ),
                ),
                checkpointB = webCheckpointRepository.save(
                    WebCheckpoint(
                        flow = webFlowRepository.save(
                            WebFlow(
                                compositions = checkpointB.map {
                                    WebComposition(
                                        resource = it.resource,
                                        widthPx = it.widthPx,
                                        delayMs = it.delayMs,
                                    )
                                },
                            ),
                        ),
                    ),
                ),
            ),
        )

        return CreateWebRegCommand.Response.Ok(
            webReg = entity,
        )
    }

}
