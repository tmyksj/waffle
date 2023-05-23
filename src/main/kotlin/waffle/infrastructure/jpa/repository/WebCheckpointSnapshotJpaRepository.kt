package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
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
     * Returns all instances with the given IDs.
     *
     * @param webCheckpointIds
     * @return
     */
    @Query("select w from wf_web_checkpoint_snapshot w where w.id.webCheckpointId in ?1")
    fun findAllByWebCheckpointId(webCheckpointIds: Iterable<String>): List<WebCheckpointSnapshotJpaEntity>

}
