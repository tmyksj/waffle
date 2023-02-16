package waffle.batch.launcher

import waffle.domain.entity.WebReg

/**
 * Launcher for web regression tests.
 */
interface WebRegLauncher {

    /**
     * Runs a given regression test.
     * The test will be persisted in each step.
     *
     * @param webReg
     */
    fun run(
        webReg: WebReg,
    )

}
