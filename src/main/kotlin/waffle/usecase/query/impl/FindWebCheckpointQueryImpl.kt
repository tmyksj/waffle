package waffle.usecase.query.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebRegRepository
import waffle.usecase.query.FindWebCheckpointQuery
import java.util.*

@Component
@Transactional
class FindWebCheckpointQueryImpl(
    private val webCheckpointRepository: WebCheckpointRepository,
    private val webRegRepository: WebRegRepository,
) : FindWebCheckpointQuery {

    override fun execute(
        id: UUID,
    ): FindWebCheckpointQuery.Response {
        val entity: WebCheckpoint = webCheckpointRepository.findById(id)
            ?: return FindWebCheckpointQuery.Response.Error(
                isNotFound = true,
            )

        // Load the output in this transaction.
        entity.output?.byteArray

        val regs: List<WebReg> = webRegRepository.findAllByCheckpoint(entity)
            .sortedByDescending { listOfNotNull(it.startedDate, it.completedDate, it.failedDate, it.createdDate).max() }

        return FindWebCheckpointQuery.Response.Ok(
            webCheckpoint = entity,
            regs = regs,
        )
    }

}
