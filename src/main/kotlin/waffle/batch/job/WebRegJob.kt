package waffle.batch.job

import org.springframework.batch.core.Job

/**
 * Executes a given regression test.
 * The test will be persisted at each step.
 */
interface WebRegJob : Job {

    /**
     * Keys for JobParameters.
     */
    enum class Keys {

        /**
         * WebReg ID.
         */
        Id,

    }

}
