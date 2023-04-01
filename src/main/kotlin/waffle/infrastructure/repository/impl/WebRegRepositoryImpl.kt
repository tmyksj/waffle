package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.core.storage.BlobStorage
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebRegRepository
import waffle.infrastructure.jpa.entity.WebRegJpaEntity
import waffle.infrastructure.jpa.repository.WebRegJpaRepository
import java.util.*

@Repository
@Transactional
class WebRegRepositoryImpl(
    private val blobStorage: BlobStorage,
    private val webCheckpointRepository: WebCheckpointRepository,
    private val webRegJpaRepository: WebRegJpaRepository,
) : WebRegRepository {

    override fun findAllByCheckpoint(checkpoint: WebCheckpoint): List<WebReg> {
        val jpaEntities: List<WebRegJpaEntity> =
            webRegJpaRepository.findAllByWebCheckpointId(listOf(checkpoint.id.toString()))

        val checkpointEntities: List<WebCheckpoint> =
            webCheckpointRepository.findAllById(
                jpaEntities.map { UUID.fromString(it.webCheckpointIdA) }
                        + jpaEntities.map { UUID.fromString(it.webCheckpointIdB) })

        val checkpoints: Map<UUID, WebCheckpoint> =
            checkpointEntities.associateBy { it.id }

        return jpaEntities.map {
            WebReg(
                id = UUID.fromString(it.id),
                checkpointA = checkNotNull(checkpoints[UUID.fromString(it.webCheckpointIdA)]),
                checkpointB = checkNotNull(checkpoints[UUID.fromString(it.webCheckpointIdB)]),
                state = WebReg.State.values()[it.state.toInt()],
                startedDate = it.startedDate,
                completedDate = it.completedDate,
                failedDate = it.failedDate,
                createdDate = it.createdDate,
                lastModifiedDate = it.lastModifiedDate,
            )
        }
    }

    override fun findById(id: UUID): WebReg? {
        val jpaEntity: WebRegJpaEntity =
            webRegJpaRepository.findByIdOrNull(id.toString()) ?: return null

        return WebReg(
            id = UUID.fromString(jpaEntity.id),
            checkpointA = checkNotNull(
                webCheckpointRepository.findById(UUID.fromString(jpaEntity.webCheckpointIdA)),
            ),
            checkpointB = checkNotNull(
                webCheckpointRepository.findById(UUID.fromString(jpaEntity.webCheckpointIdB)),
            ),
            output = jpaEntity.output?.let {
                blobStorage.findById(UUID.fromString(it))
            },
            state = WebReg.State.values()[jpaEntity.state.toInt()],
            startedDate = jpaEntity.startedDate,
            completedDate = jpaEntity.completedDate,
            failedDate = jpaEntity.failedDate,
            createdDate = jpaEntity.createdDate,
            lastModifiedDate = jpaEntity.lastModifiedDate,
        )
    }

    override fun save(entity: WebReg): WebReg {
        val jpaEntity: WebRegJpaEntity =
            (webRegJpaRepository.findByIdOrNull(entity.id.toString()) ?: WebRegJpaEntity()).copy(
                id = entity.id.toString(),
                webCheckpointIdA = entity.checkpointA.id.toString(),
                webCheckpointIdB = entity.checkpointB.id.toString(),
                output = entity.output?.let {
                    blobStorage.save(it).toString()
                },
                state = WebReg.State.values().indexOf(entity.state).toLong(),
                startedDate = entity.startedDate,
                completedDate = entity.completedDate,
                failedDate = entity.failedDate,
                createdDate = entity.createdDate,
                lastModifiedDate = entity.lastModifiedDate,
            )

        webRegJpaRepository.save(jpaEntity)

        return entity
    }

}
