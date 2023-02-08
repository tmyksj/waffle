package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.infrastructure.jpa.entity.WebCheckpointJpaEntity
import waffle.infrastructure.jpa.entity.WebCheckpointSnapshotJpaEntity
import waffle.infrastructure.jpa.repository.WebCheckpointJpaRepository
import waffle.infrastructure.jpa.repository.WebCheckpointSnapshotJpaRepository
import java.net.URL
import java.util.*

@Repository
@Transactional
class WebCheckpointRepositoryImpl(
    private val webFlowRepository: WebFlowRepository,
    private val webCheckpointJpaRepository: WebCheckpointJpaRepository,
    private val webCheckpointSnapshotJpaRepository: WebCheckpointSnapshotJpaRepository,
) : WebCheckpointRepository {

    override fun findAllByFlow(flow: WebFlow): List<WebCheckpoint> {
        val jpaEntities: List<WebCheckpointJpaEntity> =
            webCheckpointJpaRepository.findAllByWebFlowId(listOf(flow.id.toString()))

        val flowEntity: WebFlow =
            webFlowRepository.findById(flow.id) ?: return listOf()

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(jpaEntities.map { it.id })

        val snapshots: Map<String, List<WebSnapshot>> =
            snapshotJpaEntities.groupBy(
                {
                    it.id.webCheckpointId
                },
                {
                    WebSnapshot(
                        resource = URL(it.resource),
                        widthPx = it.widthPx,
                        screenshot = it.screenshot,
                    )
                },
            )

        return jpaEntities.map {
            WebCheckpoint(
                id = UUID.fromString(it.id),
                flow = flowEntity,
                snapshots = snapshots[it.id] ?: listOf(),
                state = WebCheckpoint.State.values()[it.state.toInt()],
                startedDate = it.startedDate,
                completedDate = it.completedDate,
                failedDate = it.failedDate,
                createdDate = it.createdDate,
                lastModifiedDate = it.lastModifiedDate,
            )
        }
    }

    override fun findById(id: UUID): WebCheckpoint? {
        val jpaEntity: WebCheckpointJpaEntity =
            webCheckpointJpaRepository.findByIdOrNull(id.toString()) ?: return null

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(listOf(jpaEntity.id))

        return WebCheckpoint(
            id = UUID.fromString(jpaEntity.id),
            flow = checkNotNull(
                webFlowRepository.findById(UUID.fromString(jpaEntity.webFlowId)),
            ),
            snapshots = snapshotJpaEntities.map {
                WebSnapshot(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    screenshot = it.screenshot,
                )
            },
            state = WebCheckpoint.State.values()[jpaEntity.state.toInt()],
            startedDate = jpaEntity.startedDate,
            completedDate = jpaEntity.completedDate,
            failedDate = jpaEntity.failedDate,
            createdDate = jpaEntity.createdDate,
            lastModifiedDate = jpaEntity.lastModifiedDate,
        )
    }

    override fun save(entity: WebCheckpoint): WebCheckpoint {
        val jpaEntity: WebCheckpointJpaEntity =
            (webCheckpointJpaRepository.findByIdOrNull(entity.id.toString()) ?: WebCheckpointJpaEntity()).copy(
                id = entity.id.toString(),
                webFlowId = entity.flow.id.toString(),
                state = WebCheckpoint.State.values().indexOf(entity.state).toLong(),
                startedDate = entity.startedDate,
                completedDate = entity.completedDate,
                failedDate = entity.failedDate,
                createdDate = entity.createdDate,
                lastModifiedDate = entity.lastModifiedDate,
            )

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            entity.snapshots.mapIndexed { index, it ->
                WebCheckpointSnapshotJpaEntity(
                    id = WebCheckpointSnapshotJpaEntity.Id(
                        webCheckpointId = entity.id.toString(),
                        id = index.toLong(),
                    ),
                    resource = it.resource.toString(),
                    widthPx = it.widthPx,
                    screenshot = it.screenshot,
                )
            }

        webCheckpointSnapshotJpaRepository.deleteAllByWebCheckpointId(listOf(jpaEntity.id))

        webCheckpointJpaRepository.save(jpaEntity)
        webCheckpointSnapshotJpaRepository.saveAll(snapshotJpaEntities)

        return entity
    }

}
