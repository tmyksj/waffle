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
import waffle.batch.launcher.WebRegLauncher
import waffle.core.component.ContentTypeComponent
import waffle.usecase.command.CreateWebRegCommand
import waffle.usecase.query.FindWebRegQuery
import waffle.web.form.webreg.CreateForm
import waffle.web.form.webreg.DetailsForm
import waffle.web.form.webreg.OutputForm
import waffle.web.form.webreg.QuickstartForm
import java.net.URL
import java.util.*

/**
 * Controller for regression test for Web.
 */
@Controller
class WebRegController(
    private val webRegLauncher: WebRegLauncher,
    private val contentTypeComponent: ContentTypeComponent,
    private val createWebRegCommand: CreateWebRegCommand,
    private val findWebRegQuery: FindWebRegQuery,
) {

    /**
     * GET: /WebReg
     *
     * Renders a create form page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg"])
    fun createForm(
        createForm: CreateForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebReg/createForm"
        }
    }

    /**
     * POST: /WebReg
     *
     * Creates an entity.
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/WebReg"])
    fun create(
        @Validated createForm: CreateForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            return ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebReg/createForm"
            }
        }

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = UUID.fromString(createForm.checkpointA),
            checkpointB = UUID.fromString(createForm.checkpointB),
        )

        return if (response is CreateWebRegCommand.Response.Ok) {
            // Run a regression test after creating WebReg.
            webRegLauncher.run(response.webReg)

            ModelAndView().apply {
                status = HttpStatus.SEE_OTHER
                viewName = "redirect:/WebReg/${response.webReg.id}"
            }
        } else {
            ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebReg/createForm"
            }
        }
    }

    /**
     * GET: /WebReg/Quickstart
     *
     * Renders a quickstart form page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg/Quickstart"])
    fun quickstartForm(
        quickstartForm: QuickstartForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebReg/quickstartForm"
        }
    }

    /**
     * POST: /WebReg/Quickstart
     *
     * Creates an entity.
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/WebReg/Quickstart"])
    fun quickstart(
        @Validated quickstartForm: QuickstartForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            return ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebReg/quickstartForm"
            }
        }

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = quickstartForm.checkpointA.map {
                CreateWebRegCommand.WebComposition(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            },
            checkpointB = quickstartForm.checkpointB.map {
                CreateWebRegCommand.WebComposition(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            },
        )

        return if (response is CreateWebRegCommand.Response.Ok) {
            // Run a regression test after creating WebReg.
            webRegLauncher.run(response.webReg)

            ModelAndView().apply {
                status = HttpStatus.SEE_OTHER
                viewName = "redirect:/WebReg/${response.webReg.id}"
            }
        } else {
            ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebReg/quickstartForm"
            }
        }
    }

    /**
     * GET: /WebReg/{id}
     *
     * Renders a details page by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg/{id}"])
    fun details(
        @Validated detailsForm: DetailsForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebRegQuery.Response = findWebRegQuery.execute(
            id = UUID.fromString(detailsForm.id),
        )

        return if (response is FindWebRegQuery.Response.Ok) {
            ModelAndView().apply {
                addObject("webReg", response.webReg)

                status = HttpStatus.OK
                viewName = "WebReg/details"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * GET: /WebReg/{id}/Output
     *
     * Responds an output by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg/{id}/Output"])
    fun output(
        @Validated outputForm: OutputForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebRegQuery.Response = findWebRegQuery.execute(
            id = UUID.fromString(outputForm.id),
        )

        if (response is FindWebRegQuery.Response.Ok) {
            val output: ByteArray = response.webReg.output?.byteArray
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

            val type: String = contentTypeComponent.guessType(output)
            val extension: String = contentTypeComponent.guessExtension(output)

            return ResponseEntity.ok()
                .header("Content-Type", type)
                .header("Content-Disposition", "attachment; filename=\"${response.webReg.id}${extension}\"")
                .body(output)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

}
