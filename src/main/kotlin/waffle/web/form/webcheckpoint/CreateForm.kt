package waffle.web.form.webcheckpoint

import waffle.web.controller.WebCheckpointController
import java.util.*

/**
 * Form for [WebCheckpointController.create].
 */
data class CreateForm(

    /**
     * WebFlow ID.
     */
    val flowId: UUID = UUID.randomUUID(),

    )
