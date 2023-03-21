package waffle.batch.job

import org.springframework.batch.core.Job

/**
 * Executes a creation of the checkpoint.
 */
interface WebCheckpointJob : Job {

    /**
     * Keys for JobParameters.
     */
    enum class Keys {

        /**
         * WebCheckpoint ID.
         */
        Id,

    }

}
