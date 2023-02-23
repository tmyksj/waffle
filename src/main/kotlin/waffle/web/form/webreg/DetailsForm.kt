package waffle.web.form.webreg

import waffle.web.controller.WebRegController
import java.util.*

/**
 * Form for [WebRegController.details].
 */
data class DetailsForm(

    /**
     * WebReg ID.
     */
    val id: UUID = UUID.randomUUID(),

    )
