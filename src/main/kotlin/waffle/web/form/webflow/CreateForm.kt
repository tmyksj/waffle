package waffle.web.form.webflow

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebFlowController

/**
 * Form for [WebFlowController.create].
 */
data class CreateForm(

    /**
     * Compositions for creating a checkpoint.
     */
    @field:NotEmpty
    @field:Valid
    val compositions: List<WebFlowComposition> = mutableListOf(),

    ) {

    /**
     * Composition for a page.
     */
    data class WebFlowComposition(

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
         * Waiting time before accessing to a page (milliseconds).
         */
        @field:Max(value = 60000)
        @field:Min(value = 0)
        var delayMs: Long = 0,

        )

}
