package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebCheckpointJpaEntity

/**
 * Repository for WebCheckpointJpaEntity.
 */
@Repository
@Transactional
interface WebCheckpointJpaRepository : JpaRepository<WebCheckpointJpaEntity, String> {

    /**
     * Returns all instances with the given web flow IDs.
     *
     * @param webFlowIds
     * @return
     */
    @Query("select w from wf_web_checkpoint w where w.webFlowId in ?1")
    fun findAllByWebFlowId(webFlowIds: Iterable<String>): List<WebCheckpointJpaEntity>

}
