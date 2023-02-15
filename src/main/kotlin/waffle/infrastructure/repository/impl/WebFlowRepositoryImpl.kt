package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebFlowRepository
import waffle.infrastructure.jpa.entity.WebFlowCompositionJpaEntity
import waffle.infrastructure.jpa.entity.WebFlowJpaEntity
import waffle.infrastructure.jpa.repository.WebFlowCompositionJpaRepository
import waffle.infrastructure.jpa.repository.WebFlowJpaRepository
import java.net.URL
import java.util.*

@Repository
@Transactional
class WebFlowRepositoryImpl(
    private val webFlowCompositionJpaRepository: WebFlowCompositionJpaRepository,
    private val webFlowJpaRepository: WebFlowJpaRepository,
) : WebFlowRepository {

    override fun findById(id: UUID): WebFlow? {
        val jpaEntity: WebFlowJpaEntity =
            webFlowJpaRepository.findByIdOrNull(id.toString()) ?: return null

        val compositionJpaEntities: List<WebFlowCompositionJpaEntity> =
            webFlowCompositionJpaRepository.findAllByWebFlowId(listOf(jpaEntity.id))

        return WebFlow(
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

    override fun save(entity: WebFlow): WebFlow {
        val jpaEntity: WebFlowJpaEntity =
            (webFlowJpaRepository.findByIdOrNull(entity.id.toString()) ?: WebFlowJpaEntity()).copy(
                id = entity.id.toString(),
                createdDate = entity.createdDate,
                lastModifiedDate = entity.lastModifiedDate,
            )

        val compositionJpaEntities: List<WebFlowCompositionJpaEntity> =
            entity.compositions.mapIndexed { index, it ->
                WebFlowCompositionJpaEntity(
                    id = WebFlowCompositionJpaEntity.Id(
                        webFlowId = entity.id.toString(),
                        idx = index.toLong(),
                    ),
                    resource = it.resource.toString(),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            }

        webFlowCompositionJpaRepository.deleteAllByWebFlowId(listOf(jpaEntity.id))

        webFlowJpaRepository.save(jpaEntity)
        webFlowCompositionJpaRepository.saveAll(compositionJpaEntities)

        return entity
    }

}
