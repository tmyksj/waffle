package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.BlobJpaEntity

/**
 * Repository for [BlobJpaEntity].
 */
@Repository
@Transactional
interface BlobJpaRepository : JpaRepository<BlobJpaEntity, String>
