package waffle.domain.service

import waffle.domain.entity.WebReg

/**
 * Service for web regression tests.
 */
interface WebRegService {

    /**
     * Runs a given regression test.
     * The test will be persisted in each step.
     * @param webReg
     * @return the result.
     */
    fun run(webReg: WebReg): WebReg

}
