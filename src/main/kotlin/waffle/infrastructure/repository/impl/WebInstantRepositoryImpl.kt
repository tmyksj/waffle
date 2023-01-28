package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebInstant
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebInstantRepository
import waffle.infrastructure.jpa.entity.WebInstantCompositionJpaEntity
import waffle.infrastructure.jpa.entity.WebInstantJpaEntity
import waffle.infrastructure.jpa.repository.WebInstantCompositionJpaRepository
import waffle.infrastructure.jpa.repository.WebInstantJpaRepository
import java.net.URL
import java.util.*

@Repository
@Transactional
class WebInstantRepositoryImpl(
    private val webInstantCompositionJpaRepository: WebInstantCompositionJpaRepository,
    private val webInstantJpaRepository: WebInstantJpaRepository,
) : WebInstantRepository {

    override fun findById(id: UUID): WebInstant? {
        val jpaEntity: WebInstantJpaEntity =
            webInstantJpaRepository.findByIdOrNull(id.toString()) ?: return null

        val compositionJpaEntities: List<WebInstantCompositionJpaEntity> =
            webInstantCompositionJpaRepository.findAllByWebInstantId(listOf(jpaEntity.id))

        return WebInstant(
            id = UUID.fromString(jpaEntity.id),
            compositions = compositionJpaEntities.map {
                WebComposition(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            },
            createdDate = jpaEntity.createdDate,
            lastModifiedDate = jpaEntity.lastModifiedDate,
        )
    }

    override fun save(entity: WebInstant): WebInstant {
        val jpaEntity: WebInstantJpaEntity =
            (webInstantJpaRepository.findByIdOrNull(entity.id.toString()) ?: WebInstantJpaEntity()).copy(
                id = entity.id.toString(),
                createdDate = entity.createdDate,
                lastModifiedDate = entity.lastModifiedDate,
            )

        val compositionJpaEntities: List<WebInstantCompositionJpaEntity> =
            entity.compositions.mapIndexed { index, it ->
                WebInstantCompositionJpaEntity(
                    id = WebInstantCompositionJpaEntity.Id(
                        webInstantId = entity.id.toString(),
                        id = index.toLong(),
                    ),
                    resource = it.resource.toString(),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            }

        webInstantCompositionJpaRepository.deleteAllByWebInstantId(listOf(jpaEntity.id))

        webInstantJpaRepository.save(jpaEntity)
        webInstantCompositionJpaRepository.saveAll(compositionJpaEntities)

        return entity
    }

}
