package waffle.web.form.webflow

import jakarta.validation.constraints.Pattern
import waffle.web.controller.WebFlowController

/**
 * Form for [WebFlowController.details].
 */
data class DetailsForm(

    /**
     * WebFlow ID.
     */
    @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    val id: String = "",

    )
