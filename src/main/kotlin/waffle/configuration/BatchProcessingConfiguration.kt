package waffle.configuration

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableBatchProcessing
class BatchProcessingConfiguration {

    @Bean
    fun jobLauncher(
        jobRepository: JobRepository,
    ): JobLauncher {
        val taskExecutor: TaskExecutor =
            ThreadPoolTaskExecutor().apply {
                initialize()
            }

        return TaskExecutorJobLauncher().apply {
            setJobRepository(jobRepository)
            setTaskExecutor(taskExecutor)
        }
    }

}
