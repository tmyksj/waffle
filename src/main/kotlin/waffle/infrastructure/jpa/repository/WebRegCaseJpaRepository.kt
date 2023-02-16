package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebRegCaseJpaEntity

/**
 * Repository for WebRegCaseJpaEntity.
 */
@Repository
@Transactional
interface WebRegCaseJpaRepository : JpaRepository<WebRegCaseJpaEntity, WebRegCaseJpaEntity.Id> {

    /**
     * Deletes all instances with the given IDs.
     *
     * @param webRegIds
     * @return
     */
    @Modifying
    @Query("delete from wf_web_reg_case w where w.id.webRegId in ?1")
    fun deleteAllByWebRegId(webRegIds: Iterable<String>)

    /**
     * Returns all instances with the given IDs.
     *
     * @param webRegIds
     * @return
     */
    @Query("select w from wf_web_reg_case w where w.id.webRegId in ?1")
    fun findAllByWebRegId(webRegIds: Iterable<String>): List<WebRegCaseJpaEntity>

}
