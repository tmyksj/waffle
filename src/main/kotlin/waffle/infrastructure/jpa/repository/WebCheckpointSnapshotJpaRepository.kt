package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebCheckpointSnapshotJpaEntity

/**
 * Repository for WebCheckpointSnapshotJpaEntity.
 */
@Repository
@Transactional
interface WebCheckpointSnapshotJpaRepository :
    JpaRepository<WebCheckpointSnapshotJpaEntity, WebCheckpointSnapshotJpaEntity.Id>
