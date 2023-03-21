package waffle.batch.launcher

import waffle.batch.job.WebCheckpointJob
import waffle.domain.entity.WebCheckpoint

/**
 * Launcher for [WebCheckpointJob]
 */
interface WebCheckpointLauncher {

    /**
     * Runs a job with given arguments.
     *
     * @param webCheckpoint
     */
    fun run(
        webCheckpoint: WebCheckpoint,
    )

}
