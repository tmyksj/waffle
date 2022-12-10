package waffle.infrastructure.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import waffle.infrastructure.jpa.entity.WebRegJpaEntity
import java.util.*

/**
 * Repository for WebRegJpaEntity.
 */
@Repository
@Transactional
interface WebRegJpaRepository : JpaRepository<WebRegJpaEntity, UUID>
