package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.usecase.command.CreateWebRegCommand

@Component
@Transactional
class CreateWebRegCommandImpl(
    private val webRegRepository: WebRegRepository,
) : CreateWebRegCommand {

    override fun execute(
        webRegCases: List<CreateWebRegCommand.WebRegCase>,
    ): CreateWebRegCommand.Response {
        val entity: WebReg = webRegRepository.save(
            WebReg(
                cases = webRegCases.map {
                    WebReg.Case(
                        expected = WebReg.Composition(
                            resource = it.expected.resource,
                            delayMs = it.expected.delayMs,
                        ),
                        actual = WebReg.Composition(
                            resource = it.actual.resource,
                            delayMs = it.actual.delayMs,
                        ),
                    )
                },
            ),
        )

        return CreateWebRegCommand.Response.Ok(
            webReg = entity,
        )
    }

}
