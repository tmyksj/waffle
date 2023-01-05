package waffle.batch.tasklet

import org.springframework.batch.core.step.tasklet.Tasklet

/**
 * Executes a given regression test.
 * The test will be persisted at each step.
 */
interface WebRegTasklet : Tasklet {

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
