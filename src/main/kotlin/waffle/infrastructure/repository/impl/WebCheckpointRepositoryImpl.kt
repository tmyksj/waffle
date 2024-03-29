package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.core.storage.BlobStorage
import waffle.core.type.Blob
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
    private val blobStorage: BlobStorage,
    private val webFlowRepository: WebFlowRepository,
    private val webCheckpointJpaRepository: WebCheckpointJpaRepository,
    private val webCheckpointSnapshotJpaRepository: WebCheckpointSnapshotJpaRepository,
) : WebCheckpointRepository {

    override fun findAllByFlow(flow: WebFlow): List<WebCheckpoint> {
        val jpaEntities: List<WebCheckpointJpaEntity> =
            webCheckpointJpaRepository.findAllByWebFlowId(listOf(flow.id.toString()))

        val flowEntity: WebFlow =
            webFlowRepository.findById(flow.id) ?: return listOf()

        val outputs: Map<UUID, Blob> =
            blobStorage.findAllById(jpaEntities.mapNotNull { it.output }.map { UUID.fromString(it) })

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(jpaEntities.map { it.id })

        val screenshots: Map<UUID, Blob> =
            blobStorage.findAllById(snapshotJpaEntities.map { UUID.fromString(it.screenshot) })

        val snapshots: Map<String, List<WebSnapshot>> =
            snapshotJpaEntities.groupBy(
                {
                    it.id.webCheckpointId
                },
                {
                    WebSnapshot(
                        resource = URL(it.resource),
                        widthPx = it.widthPx,
                        heightPx = it.heightPx,
                        screenshot = checkNotNull(screenshots[UUID.fromString(it.screenshot)]),
                    )
                },
            )

        return jpaEntities.map { jpaEntity ->
            WebCheckpoint(
                id = UUID.fromString(jpaEntity.id),
                flow = flowEntity,
                output = jpaEntity.output?.let {
                    outputs[UUID.fromString(it)]
                },
                snapshots = snapshots[jpaEntity.id] ?: listOf(),
                state = WebCheckpoint.State.values()[jpaEntity.state.toInt()],
                startedDate = jpaEntity.startedDate,
                completedDate = jpaEntity.completedDate,
                failedDate = jpaEntity.failedDate,
                createdDate = jpaEntity.createdDate,
                lastModifiedDate = jpaEntity.lastModifiedDate,
            )
        }
    }

    override fun findAllById(ids: List<UUID>): List<WebCheckpoint> {
        val jpaEntities: List<WebCheckpointJpaEntity> =
            webCheckpointJpaRepository.findAllById(ids.map { it.toString() })

        val flowEntities: List<WebFlow> =
            webFlowRepository.findAllById(jpaEntities.map { UUID.fromString(it.webFlowId) })

        val flows: Map<UUID, WebFlow> =
            flowEntities.associateBy { it.id }

        val outputs: Map<UUID, Blob> =
            blobStorage.findAllById(jpaEntities.mapNotNull { it.output }.map { UUID.fromString(it) })

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(jpaEntities.map { it.id })

        val screenshots: Map<UUID, Blob> =
            blobStorage.findAllById(snapshotJpaEntities.map { UUID.fromString(it.screenshot) })

        val snapshots: Map<String, List<WebSnapshot>> =
            snapshotJpaEntities.groupBy(
                {
                    it.id.webCheckpointId
                },
                {
                    WebSnapshot(
                        resource = URL(it.resource),
                        widthPx = it.widthPx,
                        heightPx = it.heightPx,
                        screenshot = checkNotNull(screenshots[UUID.fromString(it.screenshot)]),
                    )
                },
            )

        return jpaEntities.map { jpaEntity ->
            WebCheckpoint(
                id = UUID.fromString(jpaEntity.id),
                flow = checkNotNull(flows[UUID.fromString(jpaEntity.webFlowId)]),
                output = jpaEntity.output?.let {
                    outputs[UUID.fromString(it)]
                },
                snapshots = snapshots[jpaEntity.id] ?: listOf(),
                state = WebCheckpoint.State.values()[jpaEntity.state.toInt()],
                startedDate = jpaEntity.startedDate,
                completedDate = jpaEntity.completedDate,
                failedDate = jpaEntity.failedDate,
                createdDate = jpaEntity.createdDate,
                lastModifiedDate = jpaEntity.lastModifiedDate,
            )
        }
    }

    override fun findById(id: UUID): WebCheckpoint? {
        val jpaEntity: WebCheckpointJpaEntity =
            webCheckpointJpaRepository.findByIdOrNull(id.toString()) ?: return null

        val snapshotJpaEntities: List<WebCheckpointSnapshotJpaEntity> =
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(listOf(jpaEntity.id))

        val screenshots: Map<UUID, Blob> =
            blobStorage.findAllById(snapshotJpaEntities.map { UUID.fromString(it.screenshot) })

        return WebCheckpoint(
            id = UUID.fromString(jpaEntity.id),
            flow = checkNotNull(
                webFlowRepository.findById(UUID.fromString(jpaEntity.webFlowId)),
            ),
            output = jpaEntity.output?.let {
                blobStorage.findById(UUID.fromString(it))
            },
            snapshots = snapshotJpaEntities.map {
                WebSnapshot(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    heightPx = it.heightPx,
                    screenshot = checkNotNull(screenshots[UUID.fromString(it.screenshot)]),
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
                output = entity.output?.let {
                    blobStorage.save(it).toString()
                },
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
                        idx = index.toLong(),
                    ),
                    resource = it.resource.toString(),
                    widthPx = it.widthPx,
                    heightPx = it.heightPx,
                    screenshot = blobStorage.save(it.screenshot).toString(),
                )
            }

        webCheckpointSnapshotJpaRepository.deleteAll(
            webCheckpointSnapshotJpaRepository.findAllByWebCheckpointId(listOf(jpaEntity.id)),
        )

        webCheckpointJpaRepository.save(jpaEntity)
        webCheckpointSnapshotJpaRepository.saveAll(snapshotJpaEntities)

        return entity
    }

}
