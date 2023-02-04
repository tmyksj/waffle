package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import waffle.domain.model.WebComposition
import waffle.usecase.command.CreateWebFlowCommand
import waffle.usecase.query.FindWebFlowQuery
import waffle.web.form.webflow.CreateForm
import waffle.web.form.webflow.DetailsForm
import java.net.URL

/**
 * Controller for a WebFlow entity.
 */
@Controller
class WebFlowController(
    private val createWebFlowCommand: CreateWebFlowCommand,
    private val findWebFlowQuery: FindWebFlowQuery,
) {

    /**
     * GET: /WebFlow
     *
     * Renders an index page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebFlow"])
    fun index(
        createForm: CreateForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebFlow/index"
        }
    }

    /**
     * POST: /WebFlow
     *
     * Creates an entity.
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/WebFlow"])
    fun create(
        @Validated createForm: CreateForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            return ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebFlow/index"
            }
        }

        val response: CreateWebFlowCommand.Response = createWebFlowCommand.execute(
            compositions = createForm.compositions.map {
                WebComposition(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            },
        )

        return if (response is CreateWebFlowCommand.Response.Ok) {
            ModelAndView().apply {
                status = HttpStatus.SEE_OTHER
                viewName = "redirect:/WebFlow/${response.webFlow.id}"
            }
        } else {
            ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebFlow/index"
            }
        }
    }

    /**
     * GET: /WebFlow/{id}
     *
     * Renders a details page by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebFlow/{id}"])
    fun details(
        @Validated detailsForm: DetailsForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasErrors()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebFlowQuery.Response = findWebFlowQuery.execute(
            id = detailsForm.id,
        )

        return if (response is FindWebFlowQuery.Response.Ok) {
            ModelAndView().apply {
                addObject("webFlow", response.webFlow)

                status = HttpStatus.OK
                viewName = "WebFlow/details"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

}