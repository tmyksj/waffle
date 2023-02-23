package waffle.web.form.webreg

import waffle.web.controller.WebRegController
import java.util.*

/**
 * Form for [WebRegController.result].
 */
data class ResultForm(

    /**
     * WebReg ID.
     */
    val id: UUID = UUID.randomUUID(),

    )
