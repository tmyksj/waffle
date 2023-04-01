package waffle.web.form.webreg

import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebRegController

/**
 * Form for [WebRegController.create].
 */
data class CreateForm(

    /**
     * WebCheckpoint A.
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val checkpointA: String = "",

    /**
     * WebCheckpoint B.
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val checkpointB: String = "",

    )
