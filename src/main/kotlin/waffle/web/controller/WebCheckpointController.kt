package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import waffle.batch.launcher.WebCheckpointLauncher
import waffle.core.component.ContentTypeComponent
import waffle.usecase.command.CreateWebCheckpointCommand
import waffle.usecase.query.FindWebCheckpointQuery
import waffle.web.form.webcheckpoint.CreateForm
import waffle.web.form.webcheckpoint.DetailsForm
import waffle.web.form.webcheckpoint.OutputForm
import java.util.*

/**
 * Controller for a WebCheckpoint entity.
 */
@Controller
class WebCheckpointController(
    private val webCheckpointLauncher: WebCheckpointLauncher,
    private val contentTypeComponent: ContentTypeComponent,
    private val createWebCheckpointCommand: CreateWebCheckpointCommand,
    private val findWebCheckpointQuery: FindWebCheckpointQuery,
) {

    /**
     * GET: /WebCheckpoint
     *
     * Renders a create form page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebCheckpoint"])
    fun createForm(
        createForm: CreateForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebCheckpoint/createForm"
        }
    }

    /**
     * POST: /WebCheckpoint
     *
     * Creates an entity.
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/WebCheckpoint"])
    fun create(
        @Validated createForm: CreateForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            return ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebCheckpoint/createForm"
            }
        }

        val response: CreateWebCheckpointCommand.Response = createWebCheckpointCommand.execute(
            flow = UUID.fromString(createForm.flow),
        )

        return if (response is CreateWebCheckpointCommand.Response.Ok) {
            // Run a creation after creating WebCheckpoint.
            webCheckpointLauncher.run(response.webCheckpoint)

            ModelAndView().apply {
                status = HttpStatus.SEE_OTHER
                viewName = "redirect:/WebCheckpoint/${response.webCheckpoint.id}"
            }
        } else {
            ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebCheckpoint/createForm"
            }
        }
    }

    /**
     * GET: /WebCheckpoint/{id}
     *
     * Renders a details page by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebCheckpoint/{id}"])
    fun details(
        @Validated detailsForm: DetailsForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebCheckpointQuery.Response = findWebCheckpointQuery.execute(
            id = UUID.fromString(detailsForm.id),
        )

        return if (response is FindWebCheckpointQuery.Response.Ok) {
            ModelAndView().apply {
                addObject("webCheckpoint", response.webCheckpoint)
                addObject("regs", response.regs)

                status = HttpStatus.OK
                viewName = "WebCheckpoint/details"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * GET: /WebCheckpoint/{id}/Output
     *
     * Responds an output by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebCheckpoint/{id}/Output"])
    fun output(
        @Validated outputForm: OutputForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebCheckpointQuery.Response = findWebCheckpointQuery.execute(
            id = UUID.fromString(outputForm.id),
        )

        if (response is FindWebCheckpointQuery.Response.Ok) {
            val output: ByteArray = response.webCheckpoint.output?.byteArray
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

            val type: String = contentTypeComponent.guessType(output)
            val extension: String = contentTypeComponent.guessExtension(output)

            return ResponseEntity.ok()
                .header("Content-Type", type)
                .header("Content-Disposition", "attachment; filename=\"${response.webCheckpoint.id}${extension}\"")
                .body(output)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

}
