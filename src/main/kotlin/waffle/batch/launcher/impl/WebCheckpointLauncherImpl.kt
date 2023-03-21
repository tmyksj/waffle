package waffle.batch.launcher.impl

import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component
import waffle.batch.job.WebCheckpointJob
import waffle.batch.launcher.WebCheckpointLauncher
import waffle.domain.entity.WebCheckpoint

@Component
class WebCheckpointLauncherImpl(
    private val jobLauncher: JobLauncher,
    private val webCheckpointJob: WebCheckpointJob,
) : WebCheckpointLauncher {

    override fun run(
        webCheckpoint: WebCheckpoint,
    ) {
        jobLauncher.run(
            webCheckpointJob,
            JobParameters(
                mapOf(
                    WebCheckpointJob.Keys.Id.toString() to JobParameter(
                        webCheckpoint.id.toString(),
                        String::class.java,
                    ),
                ),
            ),
        )
    }

}
