package waffle.infrastructure.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import waffle.core.type.Blob
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.domain.service.ReportingService
import waffle.domain.service.WebCheckpointService
import waffle.domain.service.WebRegService

@Service
class WebRegServiceImpl(
    private val platformTransactionManager: PlatformTransactionManager,
    private val reportingService: ReportingService,
    private val webCheckpointService: WebCheckpointService,
    private val webRegRepository: WebRegRepository,
) : WebRegService {

    override fun run(entity: WebReg) {
        val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            transactionManager = platformTransactionManager
        }

        try {
            val e1: WebReg = transactionTemplate.execute {
                webRegRepository.save(entity.start())
            }.let { checkNotNull(it) }

            if (e1.checkpointA.isReady) {
                webCheckpointService.create(e1.checkpointA)
            }

            if (e1.checkpointB.isReady) {
                webCheckpointService.create(e1.checkpointB)
            }

            val e2: WebReg = transactionTemplate.execute {
                webRegRepository.findById(e1.id)
            }.let { checkNotNull(it) }

            if (e2.checkpointA.isCompleted && e2.checkpointB.isCompleted) {
                val output: ByteArray = reportingService.compareImages(
                    e2.checkpointA.snapshots.map { it.screenshot.byteArray },
                    e2.checkpointB.snapshots.map { it.screenshot.byteArray },
                )

                transactionTemplate.execute {
                    webRegRepository.save(e2.complete(Blob { output.inputStream() }))
                }
            } else {
                transactionTemplate.execute {
                    webRegRepository.save(e2.fail())
                }
            }
        } catch (e: Exception) {
            transactionTemplate.execute {
                webRegRepository.save(entity.fail())
            }
        }
    }

}
