package waffle.infrastructure.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import waffle.core.component.BrowserComponent
import waffle.core.type.Blob
import waffle.domain.entity.WebCheckpoint
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.service.WebCheckpointService

@Service
class WebCheckpointServiceImpl(
    private val platformTransactionManager: PlatformTransactionManager,
    private val browserComponent: BrowserComponent,
    private val webCheckpointRepository: WebCheckpointRepository,
) : WebCheckpointService {

    override fun create(entity: WebCheckpoint) {
        val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            transactionManager = platformTransactionManager
        }

        try {
            val e1: WebCheckpoint = transactionTemplate.execute {
                webCheckpointRepository.save(entity.start())
            }.let { checkNotNull(it) }

            val snapshots: List<WebSnapshot> = e1.flow.compositions.map {
                WebSnapshot(
                    resource = it.resource,
                    widthPx = it.widthPx,
                    screenshot = Blob {
                        Thread.sleep(it.delayMs)

                        browserComponent.captureScreenshot(
                            url = it.resource,
                            width = it.widthPx.toInt(),
                        ).inputStream()
                    },
                )
            }

            transactionTemplate.execute {
                webCheckpointRepository.save(e1.complete(snapshots))
            }
        } catch (e: Exception) {
            transactionTemplate.execute {
                webCheckpointRepository.save(entity.fail())
            }
        }
    }

}
