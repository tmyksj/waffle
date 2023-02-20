package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
import waffle.domain.repository.WebCheckpointRepository
import waffle.usecase.query.FindWebCheckpointQuery
import java.util.*

@Component
@Transactional
class FindWebCheckpointQueryImpl(
    private val webCheckpointRepository: WebCheckpointRepository,
) : FindWebCheckpointQuery {

    override fun execute(
        id: UUID,
    ): FindWebCheckpointQuery.Response {
        val entity: WebCheckpoint = webCheckpointRepository.findById(id)
            ?: return FindWebCheckpointQuery.Response.Error(
                isNotFound = true,
            )

        return FindWebCheckpointQuery.Response.Ok(
            webCheckpoint = entity,
        )
    }

}
