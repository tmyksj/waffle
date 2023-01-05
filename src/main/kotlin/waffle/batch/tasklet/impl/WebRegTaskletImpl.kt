package waffle.batch.tasklet.impl

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import waffle.batch.tasklet.WebRegTasklet
import waffle.component.BrowserComponent
import waffle.component.ReportingComponent
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import java.util.*

@Component
class WebRegTaskletImpl(
    private val browserComponent: BrowserComponent,
    private val reportingComponent: ReportingComponent,
    private val webRegRepository: WebRegRepository,
) : WebRegTasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val jobParameters: JobParameters = checkNotNull(contribution.stepExecution.jobParameters)

        val id: UUID = UUID.fromString(jobParameters.getString(WebRegTasklet.Keys.Id.toString()))

        val entity1: WebReg = webRegRepository.findById(id)
            ?: return RepeatStatus.FINISHED

        val entity2: WebReg = webRegRepository.save(entity1.start())

        try {
            val a: List<ByteArray> = entity2.cases.map {
                browserComponent.captureScreenshot(it.expected)
            }

            val b: List<ByteArray> = entity2.cases.map {
                browserComponent.captureScreenshot(it.actual)
            }

            val result: ByteArray = reportingComponent.compareImages(a, b)

            webRegRepository.save(entity2.complete(result))
        } catch (e: Exception) {
            webRegRepository.save(entity2.fail())
        }

        return RepeatStatus.FINISHED
    }

}
