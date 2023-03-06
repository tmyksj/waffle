package waffle.web.form.webreg

import waffle.web.controller.WebRegController
import java.util.*

/**
 * Form for [WebRegController.output].
 */
data class OutputForm(

    /**
     * WebReg ID.
     */
    val id: UUID = UUID.randomUUID(),

    )
