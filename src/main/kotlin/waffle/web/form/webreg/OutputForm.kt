package waffle.web.form.webreg

import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebRegController

/**
 * Form for [WebRegController.output].
 */
data class OutputForm(

    /**
     * WebReg ID.
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val id: String = "",

    )
