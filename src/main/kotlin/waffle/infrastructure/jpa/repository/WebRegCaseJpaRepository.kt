package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebRegCaseJpaEntity

/**
 * Repository for WebRegCaseJpaEntity.
 */
@Repository
@Transactional
interface WebRegCaseJpaRepository : JpaRepository<WebRegCaseJpaEntity, WebRegCaseJpaEntity.Id>
