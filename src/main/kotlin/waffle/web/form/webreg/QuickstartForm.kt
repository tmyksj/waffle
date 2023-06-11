package waffle.web.form.webreg

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebRegController

/**
 * Form for [WebRegController.quickstart].
 */
data class QuickstartForm(

    /**
     * WebCheckpoint A.
     */
    @field:NotEmpty
    @field:Valid
    val checkpointA: List<WebComposition> = mutableListOf(),

    /**
     * WebCheckpoint B.
     */
    @field:NotEmpty
    @field:Valid
    val checkpointB: List<WebComposition> = mutableListOf(),

    ) {

    /**
     * Composition for a page.
     */
    data class WebComposition(

        /**
         * URL for a page.
         */
        @field:Pattern(regexp = "^https?://.+$")
        var resource: String = "",

        /**
         * Window width for a page (pixels).
         */
        @field:Max(value = 4000)
        @field:Min(value = 100)
        var widthPx: Long = 1920,

        /**
         * Window height for a page (pixels).
         */
        @field:Max(value = 4000)
        @field:Min(value = 100)
        var heightPx: Long = 1080,

        /**
         * Waiting time before accessing to a page (milliseconds).
         */
        @field:Max(value = 60000)
        @field:Min(value = 0)
        var delayMs: Long = 0,

        )

}
