package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import waffle.usecase.command.WebRegCommand
import waffle.usecase.query.WebRegQuery
import waffle.web.form.webreg.CreateForm
import waffle.web.form.webreg.DetailsForm
import java.net.URL

/**
 * Controller for regression test for Web.
 */
@Controller
class WebRegController(
        private val webRegCommand: WebRegCommand,
        private val webRegQuery: WebRegQuery,
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
            addObject("createForm", createForm.copy(cases = listOf(CreateForm.WebRegCase())))

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

        val response: WebRegCommand.CreateResponse = webRegCommand.create(
                webRegCases = createForm.cases.map {
                    WebRegCommand.WebRegCase(
                            expected = URL(it.expected),
                            actual = URL(it.actual),
                    )
                },
        )

        return if (response is WebRegCommand.CreateResponse.Ok) {
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

        val response: WebRegQuery.DetailsResponse = webRegQuery.details(
                id = detailsForm.id,
        )

        return if (response is WebRegQuery.DetailsResponse.Ok) {
            ModelAndView().apply {
                addObject("webReg", response.webReg)

                status = HttpStatus.OK
                viewName = "WebReg/details"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

}
