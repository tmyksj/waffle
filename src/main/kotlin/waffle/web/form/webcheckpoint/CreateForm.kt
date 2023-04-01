package waffle.web.form.webcheckpoint

import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebCheckpointController

/**
 * Form for [WebCheckpointController.create].
 */
data class CreateForm(

    /**
     * WebFlow.
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val flow: String = "",

    )
