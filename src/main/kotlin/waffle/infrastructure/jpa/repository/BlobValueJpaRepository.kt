package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.BlobValueJpaEntity

/**
 * Repository for [BlobValueJpaEntity].
 */
@Repository
@Transactional
interface BlobValueJpaRepository : JpaRepository<BlobValueJpaEntity, String>
