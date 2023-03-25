package waffle.web.form.webcheckpoint

import waffle.web.controller.WebCheckpointController
import java.util.*

/**
 * Form for [WebCheckpointController.create].
 */
data class CreateForm(

    /**
     * WebFlow.
     */
    val flow: UUID = UUID.randomUUID(),

    )
