package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.usecase.query.WebRegQuery
import java.util.*

@Component
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

}
