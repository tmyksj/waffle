package waffle.domain.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import waffle.component.BrowserComponent
import waffle.component.ReportingComponent
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.domain.service.WebRegService

@Service
@Transactional
class WebRegServiceImpl(
    private val platformTransactionManager: PlatformTransactionManager,
    private val browserComponent: BrowserComponent,
    private val reportingComponent: ReportingComponent,
    private val webRegRepository: WebRegRepository,
) : WebRegService {

    override fun run(webReg: WebReg): WebReg {
        val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            transactionManager = platformTransactionManager
        }

        val entity: WebReg = transactionTemplate.execute {
            webRegRepository.save(webReg.start())
        }.let { checkNotNull(it) }

        return try {
            val a: List<ByteArray> = entity.cases.map {
                browserComponent.captureScreenshot(it.expected)
            }

            val b: List<ByteArray> = entity.cases.map {
                browserComponent.captureScreenshot(it.actual)
            }

            val result: ByteArray = reportingComponent.compareImages(a, b)

            transactionTemplate.execute {
                webRegRepository.save(entity.complete(result))
            }.let { checkNotNull(it) }
        } catch (e: Exception) {
            transactionTemplate.execute {
                webRegRepository.save(entity.fail())
            }.let { checkNotNull(it) }
        }
    }

}
