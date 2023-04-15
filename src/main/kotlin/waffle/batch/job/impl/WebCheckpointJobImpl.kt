package waffle.batch.job.impl

import org.springframework.batch.core.*
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import waffle.batch.job.WebCheckpointJob
import waffle.domain.entity.WebCheckpoint
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.service.WebCheckpointService
import java.util.*

@Component
class WebCheckpointJobImpl(
    jobRepository: JobRepository,
    stepImpl: StepImpl,
) : WebCheckpointJob {

    private val job: Job = JobBuilder(checkNotNull(WebCheckpointJob::class.qualifiedName), jobRepository)
        .start(stepImpl)
        .build()

    override fun getName(): String {
        return job.name
    }

    override fun execute(execution: JobExecution) {
        return job.execute(execution)
    }

    @Component
    class StepImpl(
        jobRepository: JobRepository,
        platformTransactionManager: PlatformTransactionManager,
        taskletImpl: TaskletImpl,
    ) : Step {

        private val step: Step = StepBuilder(checkNotNull(StepImpl::class.qualifiedName), jobRepository)
            .tasklet(taskletImpl, platformTransactionManager)
            .build()

        override fun getName(): String {
            return step.name
        }

        override fun execute(stepExecution: StepExecution) {
            step.execute(stepExecution)
        }

    }

    @Component
    class TaskletImpl(
        private val platformTransactionManager: PlatformTransactionManager,
        private val webCheckpointRepository: WebCheckpointRepository,
        private val webCheckpointService: WebCheckpointService,
    ) : Tasklet {

        override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
            val jobParameters: JobParameters = checkNotNull(contribution.stepExecution.jobParameters)

            val id: UUID = UUID.fromString(jobParameters.getString(WebCheckpointJob.Keys.Id.toString()))

            val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
                propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
                transactionManager = platformTransactionManager
            }

            val entity: WebCheckpoint = transactionTemplate.execute {
                webCheckpointRepository.findById(id)
            } ?: return RepeatStatus.FINISHED

            webCheckpointService.create(entity)

            return RepeatStatus.FINISHED
        }

    }

}
