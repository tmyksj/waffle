package waffle.web.form.webcheckpoint

import waffle.web.controller.WebCheckpointController
import java.util.*

/**
 * Form for [WebCheckpointController.details].
 */
data class DetailsForm(

    /**
     * WebCheckpoint ID.
     */
    val id: UUID = UUID.randomUUID(),

    )
