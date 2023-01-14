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
import waffle.component.ContentTypeComponent
import waffle.usecase.command.CreateWebRegCommand
import waffle.usecase.query.FindWebRegQuery
import waffle.web.form.webreg.CreateForm
import waffle.web.form.webreg.DetailsForm
import waffle.web.form.webreg.ResultForm
import java.net.URL

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
     * Renders an index page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg"])
    fun index(
        createForm: CreateForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebReg/index"
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
                viewName = "WebReg/index"
            }
        }

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            webRegCases = createForm.cases.map {
                CreateWebRegCommand.WebRegCase(
                    expected = CreateWebRegCommand.WebRegComposition(
                        resource = URL(it.expected.resource),
                        delayMs = it.expected.delayMs,
                    ),
                    actual = CreateWebRegCommand.WebRegComposition(
                        resource = URL(it.actual.resource),
                        delayMs = it.actual.delayMs,
                    ),
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
                viewName = "WebReg/index"
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
            id = detailsForm.id,
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
     * GET: /WebReg/{id}/Result
     *
     * Responds a result by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebReg/{id}/Result"])
    fun result(
        @Validated resultForm: ResultForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebRegQuery.Response = findWebRegQuery.execute(
            id = resultForm.id,
        )

        if (response is FindWebRegQuery.Response.Ok) {
            val result: ByteArray = response.webReg.result
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

            val type: String = contentTypeComponent.guessType(result)
            val extension: String = contentTypeComponent.guessExtension(result)

            return ResponseEntity.ok()
                .header("Content-Type", type)
                .header("Content-Disposition", "attachment; filename=\"${response.webReg.id}${extension}\"")
                .body(result)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

}
