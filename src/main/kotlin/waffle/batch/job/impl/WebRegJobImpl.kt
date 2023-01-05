package waffle.batch.job.impl

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.stereotype.Component
import waffle.batch.job.WebRegJob
import waffle.batch.step.WebRegStep

@Component
class WebRegJobImpl(
    jobRepository: JobRepository,
    webRegStep: WebRegStep,
) : WebRegJob {

    private val job: Job = JobBuilder(checkNotNull(WebRegJob::class.qualifiedName), jobRepository)
        .start(webRegStep)
        .build()

    override fun getName(): String {
        return job.name
    }

    override fun execute(execution: JobExecution) {
        return job.execute(execution)
    }

}
