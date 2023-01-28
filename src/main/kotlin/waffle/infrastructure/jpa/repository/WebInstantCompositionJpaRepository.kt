package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebInstantCompositionJpaEntity

/**
 * Repository for WebInstantCompositionJpaEntity.
 */
@Repository
@Transactional
interface WebInstantCompositionJpaRepository :
    JpaRepository<WebInstantCompositionJpaEntity, WebInstantCompositionJpaEntity.Id> {

    /**
     * Deletes all instances with the given IDs.
     * @param webInstantIds
     * @return
     */
    @Modifying
    @Query("delete from wf_web_instant_composition w where w.id.webInstantId in ?1")
    fun deleteAllByWebInstantId(webInstantIds: Iterable<String>)

    /**
     * Returns all instances with the given IDs.
     * @param webInstantIds
     * @return
     */
    @Query("select w from wf_web_instant_composition w where w.id.webInstantId in ?1")
    fun findAllByWebInstantId(webInstantIds: Iterable<String>): List<WebInstantCompositionJpaEntity>

}
