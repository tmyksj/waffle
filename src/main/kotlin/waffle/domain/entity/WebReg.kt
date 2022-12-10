package waffle.domain.entity

import waffle.domain.time.now
import java.net.URL
import java.time.LocalDateTime
import java.util.*

/**
 * Regression test for Web.
 */
data class WebReg(

    /**
     * ID.
     */
    val id: UUID = UUID.randomUUID(),

    /**
     * Test cases.
     */
    val cases: List<Case> = listOf(),

    /**
     * Test result.
     */
    val result: String? = null,

    /**
     * State.
     */
    val state: State = State.Ready,

    /**
     * Date that the test was started.
     */
    val startedDate: LocalDateTime? = null,

    /**
     * Date that the test was completed.
     */
    val completedDate: LocalDateTime? = null,

    /**
     * Date that the test was failed.
     */
    val failedDate: LocalDateTime? = null,

    /**
     * Date that the entity was created.
     */
    val createdDate: LocalDateTime = now(),

    /**
     * Date that the entity was recently modified.
     */
    val lastModifiedDate: LocalDateTime = createdDate,

    ) {

    /**
     * Test case.
     */
    data class Case(

        /**
         * URL for expected page.
         */
        val expected: URL,

        /**
         * URL for actual page.
         */
        val actual: URL,

        )

    /**
     * WebReg state.
     */
    enum class State {

        /**
         * The test is ready.
         */
        Ready,

        /**
         * The test was started.
         */
        Started,

        /**
         * The test was completed.
         */
        Completed,

        /**
         * The test was failed.
         */
        Failed,

    }

}
