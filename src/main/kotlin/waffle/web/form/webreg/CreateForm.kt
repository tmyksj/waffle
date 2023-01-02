package waffle.web.form.webreg

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

/**
 * Form for #create.
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
         * URL for expected page.
         */
        @field:Pattern(regexp = "^https?://.+$")
        var expected: String = "",

        /**
         * URL for actual page.
         */
        @field:Pattern(regexp = "^https?://.+$")
        var actual: String = "",

        )

}
