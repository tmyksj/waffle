package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.usecase.query.FindWebRegQuery
import java.util.*

@Component
@Transactional
class FindWebRegQueryImpl(
    private val webRegRepository: WebRegRepository,
) : FindWebRegQuery {

    override fun execute(
        id: UUID,
    ): FindWebRegQuery.Response {
        val entity: WebReg = webRegRepository.findById(id)
            ?: return FindWebRegQuery.Response.Error(
                isNotFound = true,
            )

        return FindWebRegQuery.Response.Ok(
            webReg = entity,
        )
    }

}
