package waffle.web.form.webflow

import waffle.web.controller.WebFlowController
import java.util.*

/**
 * Form for [WebFlowController.details].
 */
data class DetailsForm(

    /**
     * WebFlow ID.
     */
    val id: UUID = UUID.randomUUID(),

    )
