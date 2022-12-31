package waffle.usecase.command.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.usecase.command.WebRegCommand

@Component
@Transactional
class WebRegCommandImpl(
        private val webRegRepository: WebRegRepository,
) : WebRegCommand {

    override fun create(
            webRegCases: List<WebRegCommand.WebRegCase>,
    ): WebRegCommand.CreateResponse {
        val entity: WebReg = webRegRepository.save(
                WebReg(
                        cases = webRegCases.map {
                            WebReg.Case(
                                    expected = it.expected,
                                    actual = it.actual,
                            )
                        },
                ),
        )

        return WebRegCommand.CreateResponse.Ok(
                webReg = entity,
        )
    }

}
