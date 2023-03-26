package waffle.web.form.webreg

import waffle.web.controller.WebRegController
import java.util.*

/**
 * Form for [WebRegController.create].
 */
data class CreateForm(

    /**
     * WebCheckpoint A.
     */
    val checkpointA: UUID = UUID.randomUUID(),

    /**
     * WebCheckpoint B.
     */
    val checkpointB: UUID = UUID.randomUUID(),

    )
