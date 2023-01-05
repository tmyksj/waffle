package waffle.batch.step.impl

import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import waffle.batch.step.WebRegStep
import waffle.batch.tasklet.WebRegTasklet

@Component
class WebRegStepImpl(
    jobRepository: JobRepository,
    platformTransactionManager: PlatformTransactionManager,
    webRegTasklet: WebRegTasklet,
) : WebRegStep {

    private val step: Step = StepBuilder(checkNotNull(WebRegStep::class.qualifiedName), jobRepository)
        .tasklet(webRegTasklet, platformTransactionManager)
        .build()

    override fun getName(): String {
        return step.name
    }

    override fun execute(stepExecution: StepExecution) {
        step.execute(stepExecution)
    }

}
