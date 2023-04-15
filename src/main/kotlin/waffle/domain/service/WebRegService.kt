package waffle.domain.service

import waffle.domain.entity.WebReg

/**
 * Service for [WebReg].
 */
interface WebRegService {

    /**
     * Runs or reruns a regression test.
     *
     * @param entity
     */
    fun run(entity: WebReg)

}
