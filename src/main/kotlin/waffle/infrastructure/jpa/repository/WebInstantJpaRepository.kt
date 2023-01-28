package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebInstantJpaEntity

/**
 * Repository for WebInstantJpaEntity.
 */
@Repository
@Transactional
interface WebInstantJpaRepository : JpaRepository<WebInstantJpaEntity, String>
