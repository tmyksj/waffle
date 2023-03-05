package waffle.batch.launcher

import waffle.batch.job.WebRegJob
import waffle.domain.entity.WebReg

/**
 * Launcher for [WebRegJob].
 */
interface WebRegLauncher {

    /**
     * Runs a job with given arguments.
     *
     * @param webReg
     */
    fun run(
        webReg: WebReg,
    )

}
