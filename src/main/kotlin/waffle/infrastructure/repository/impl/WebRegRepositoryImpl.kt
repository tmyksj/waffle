package waffle.infrastructure.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.infrastructure.jpa.entity.WebRegCaseJpaEntity
import waffle.infrastructure.jpa.entity.WebRegJpaEntity
import waffle.infrastructure.jpa.repository.WebRegCaseJpaRepository
import waffle.infrastructure.jpa.repository.WebRegJpaRepository
import java.net.URL
import java.util.*

@Repository
@Transactional
class WebRegRepositoryImpl(
    private val webRegCaseJpaRepository: WebRegCaseJpaRepository,
    private val webRegJpaRepository: WebRegJpaRepository,
) : WebRegRepository {

    override fun findById(id: UUID): WebReg? {
        val jpaEntity: WebRegJpaEntity =
            webRegJpaRepository.findByIdOrNull(id.toString()) ?: return null

        val caseJpaEntities: List<WebRegCaseJpaEntity> =
            webRegCaseJpaRepository.findAllByWebRegId(listOf(jpaEntity.id))

        return WebReg(
            id = UUID.fromString(jpaEntity.id),
            cases = caseJpaEntities.map {
                WebReg.Case(
                    expected = URL(it.expected),
                    actual = URL(it.actual),
                )
            },
            result = jpaEntity.result,
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
                result = entity.result,
                state = WebReg.State.values().indexOf(entity.state).toLong(),
                startedDate = entity.startedDate,
                completedDate = entity.completedDate,
                failedDate = entity.failedDate,
                createdDate = entity.createdDate,
                lastModifiedDate = entity.lastModifiedDate,
            )

        val caseJpaEntities: List<WebRegCaseJpaEntity> =
            entity.cases.mapIndexed { index, it ->
                WebRegCaseJpaEntity(
                    id = WebRegCaseJpaEntity.Id(
                        webRegId = entity.id.toString(),
                        id = index.toLong(),
                    ),
                    expected = it.expected.toString(),
                    actual = it.actual.toString()
                )
            }

        webRegCaseJpaRepository.deleteAllByWebRegId(listOf(jpaEntity.id))

        webRegJpaRepository.save(jpaEntity)
        webRegCaseJpaRepository.saveAll(caseJpaEntities)

        return entity
    }

}
