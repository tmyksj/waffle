package waffle.web.form.webreg

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebRegController

/**
 * Form for [WebRegController.create].
 */
data class CreateForm(

    /**
     * Test cases.
     */
    @field:NotEmpty
    @field:Valid
    val cases: List<WebRegCase> = mutableListOf(),

    ) {

    /**
     * Test case.
     */
    data class WebRegCase(

        /**
         * Composition for an expected page.
         */
        @field:Valid
        val expected: WebRegComposition = WebRegComposition(),

        /**
         * Composition for an actual page.
         */
        @field:Valid
        val actual: WebRegComposition = WebRegComposition(),

        )

    /**
     * Composition for a page.
     */
    data class WebRegComposition(

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
