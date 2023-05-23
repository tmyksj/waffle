package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebFlowCompositionJpaEntity

/**
 * Repository for [WebFlowCompositionJpaEntity].
 */
@Repository
@Transactional
interface WebFlowCompositionJpaRepository : JpaRepository<WebFlowCompositionJpaEntity, WebFlowCompositionJpaEntity.Id> {

    /**
     * Returns all instances with the given IDs.
     *
     * @param webFlowIds
     * @return
     */
    @Query("select w from wf_web_flow_composition w where w.id.webFlowId in ?1")
    fun findAllByWebFlowId(webFlowIds: Iterable<String>): List<WebFlowCompositionJpaEntity>

}
