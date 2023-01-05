package waffle.batch.launcher.impl

import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component
import waffle.batch.job.WebRegJob
import waffle.batch.launcher.WebRegLauncher
import waffle.batch.tasklet.WebRegTasklet
import waffle.domain.entity.WebReg

@Component
class WebRegLauncherImpl(
    private val jobLauncher: JobLauncher,
    private val webRegJob: WebRegJob,
) : WebRegLauncher {

    override fun run(
        webReg: WebReg,
    ) {
        jobLauncher.run(
            webRegJob,
            JobParameters(
                mapOf(
                    WebRegTasklet.Keys.Id.toString() to JobParameter(webReg.id.toString(), String::class.java),
                ),
            ),
        )
    }

}
