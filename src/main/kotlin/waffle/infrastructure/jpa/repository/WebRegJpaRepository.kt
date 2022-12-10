package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebRegJpaEntity

/**
 * Repository for WebRegJpaEntity.
 */
@Repository
@Transactional
interface WebRegJpaRepository : JpaRepository<WebRegJpaEntity, String>
