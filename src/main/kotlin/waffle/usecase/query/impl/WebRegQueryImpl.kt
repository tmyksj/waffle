package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.usecase.query.WebRegQuery
import java.util.*

@Component
@Transactional
class WebRegQueryImpl(
        private val webRegRepository: WebRegRepository,
) : WebRegQuery {

    override fun details(
            id: UUID,
    ): WebRegQuery.DetailsResponse {
        val entity: WebReg = webRegRepository.findById(id)
                ?: return WebRegQuery.DetailsResponse.Error(
                        isNotFound = true,
                )

        return WebRegQuery.DetailsResponse.Ok(
                webReg = entity,
        )
    }

    override fun result(
            id: UUID,
    ): WebRegQuery.ResultResponse {
        val entity: WebReg = webRegRepository.findById(id)
                ?: return WebRegQuery.ResultResponse.Error(
                        isNotFound = true,
                )

        val result: ByteArray = entity.result
                ?: return WebRegQuery.ResultResponse.Error(
                        isNotFound = true,
                )

        return WebRegQuery.ResultResponse.Ok(
                result = result,
        )
    }

}
