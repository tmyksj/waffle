package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebCheckpointSnapshotJpaEntity

/**
 * Repository for [WebCheckpointSnapshotJpaEntity].
 */
@Repository
@Transactional
interface WebCheckpointSnapshotJpaRepository :
    JpaRepository<WebCheckpointSnapshotJpaEntity, WebCheckpointSnapshotJpaEntity.Id> {

    /**
     * Deletes all instances with the given IDs.
     *
     * @param webCheckpointIds
     * @return
     */
    @Modifying
    @Query("delete from wf_web_checkpoint_snapshot w where w.id.webCheckpointId in ?1")
    fun deleteAllByWebCheckpointId(webCheckpointIds: Iterable<String>)

    /**
     * Returns all instances with the given IDs.
     *
     * @param webCheckpointIds
     * @return
     */
    @Query("select w from wf_web_checkpoint_snapshot w where w.id.webCheckpointId in ?1")
    fun findAllByWebCheckpointId(webCheckpointIds: Iterable<String>): List<WebCheckpointSnapshotJpaEntity>

}
