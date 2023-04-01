package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebRegJpaEntity

/**
 * Repository for [WebRegJpaEntity].
 */
@Repository
@Transactional
interface WebRegJpaRepository : JpaRepository<WebRegJpaEntity, String> {

    /**
     * Returns all instances with the given web checkpoint IDs.
     *
     * @param webCheckpointIds
     * @return
     */
    @Query("select w from wf_web_reg w where w.webCheckpointIdA in ?1 or w.webCheckpointIdB in ?1")
    fun findAllByWebCheckpointId(webCheckpointIds: Iterable<String>): List<WebRegJpaEntity>

}
