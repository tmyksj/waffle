package waffle.batch.tasklet.impl

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import waffle.batch.tasklet.WebRegTasklet
import waffle.core.component.BrowserComponent
import waffle.core.component.ReportingComponent
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import java.util.*

@Component
class WebRegTaskletImpl(
    private val platformTransactionManager: PlatformTransactionManager,
    private val browserComponent: BrowserComponent,
    private val reportingComponent: ReportingComponent,
    private val webRegRepository: WebRegRepository,
) : WebRegTasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val jobParameters: JobParameters = checkNotNull(contribution.stepExecution.jobParameters)

        val id: UUID = UUID.fromString(jobParameters.getString(WebRegTasklet.Keys.Id.toString()))

        val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            transactionManager = platformTransactionManager
        }

        val entity1: WebReg = transactionTemplate.execute {
            webRegRepository.findById(id)
        } ?: return RepeatStatus.FINISHED

        val entity2: WebReg = transactionTemplate.execute {
            webRegRepository.save(entity1.start())
        }.let { checkNotNull(it) }

        try {
            val a: List<ByteArray> = entity2.cases.map {
                Thread.sleep(it.expected.delayMs)

                browserComponent.captureScreenshot(
                    url = it.expected.resource,
                    width = it.expected.widthPx.toInt(),
                )
            }

            val b: List<ByteArray> = entity2.cases.map {
                Thread.sleep(it.actual.delayMs)

                browserComponent.captureScreenshot(
                    url = it.actual.resource,
                    width = it.actual.widthPx.toInt(),
                )
            }

            val result: ByteArray = reportingComponent.compareImages(a, b)

            transactionTemplate.execute {
                webRegRepository.save(entity2.complete(result))
            }
        } catch (e: Exception) {
            transactionTemplate.execute {
                webRegRepository.save(entity2.fail())
            }
        }

        return RepeatStatus.FINISHED
    }

}
