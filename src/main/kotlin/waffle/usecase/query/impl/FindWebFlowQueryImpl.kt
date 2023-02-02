package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebFlow
import waffle.domain.repository.WebFlowRepository
import waffle.usecase.query.FindWebFlowQuery
import java.util.*

@Component
@Transactional
class FindWebFlowQueryImpl(
    private val webFlowRepository: WebFlowRepository,
) : FindWebFlowQuery {

    override fun execute(
        id: UUID,
    ): FindWebFlowQuery.Response {
        val entity: WebFlow = webFlowRepository.findById(id)
            ?: return FindWebFlowQuery.Response.Error(
                isNotFound = true,
            )

        return FindWebFlowQuery.Response.Ok(
            webFlow = entity,
        )
    }

}
