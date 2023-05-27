package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import waffle.usecase.command.CreateWebFlowCommand
import waffle.usecase.command.ModifyWebFlowCommand
import waffle.usecase.query.FindWebFlowQuery
import waffle.web.form.webflow.CreateForm
import waffle.web.form.webflow.DetailsForm
import waffle.web.form.webflow.ModifyForm
import waffle.web.validation.ValidationComponent
import java.net.URL
import java.util.*

/**
 * Controller for a WebFlow entity.
 */
@Controller
class WebFlowController(
    private val createWebFlowCommand: CreateWebFlowCommand,
    private val modifyWebFlowCommand: ModifyWebFlowCommand,
    private val findWebFlowQuery: FindWebFlowQuery,
    private val validationComponent: ValidationComponent,
) {

    /**
     * GET: /WebFlow
     *
     * Renders a create form page.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebFlow"])
    fun createForm(
        createForm: CreateForm,
    ): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "WebFlow/createForm"
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
                viewName = "WebFlow/createForm"
            }
        }

        val response: CreateWebFlowCommand.Response = createWebFlowCommand.execute(
            compositions = createForm.compositions.map {
                CreateWebFlowCommand.WebComposition(
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
                viewName = "WebFlow/createForm"
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
            id = UUID.fromString(detailsForm.id),
        )

        return if (response is FindWebFlowQuery.Response.Ok) {
            ModelAndView().apply {
                addObject("webFlow", response.webFlow)
                addObject("checkpoints", response.checkpoints)

                status = HttpStatus.OK
                viewName = "WebFlow/details"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * GET: /WebFlow/{id}/Modify
     *
     * Renders a modify form page by its id or responds HTTP status 404 if none found.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/WebFlow/{id}/Modify"])
    fun modifyForm(
        modifyForm: ModifyForm,
    ): Any {
        val bindingResult: BindingResult = validationComponent.validate(modifyForm)

        if (bindingResult.hasFieldErrors("id")) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        val response: FindWebFlowQuery.Response = findWebFlowQuery.execute(
            id = UUID.fromString(modifyForm.id),
        )

        return if (response is FindWebFlowQuery.Response.Ok) {
            val form: ModifyForm = modifyForm.copy(
                compositions = modifyForm.compositions.ifEmpty {
                    response.webFlow.compositions.map {
                        ModifyForm.WebComposition(
                            resource = it.resource.toString(),
                            widthPx = it.widthPx,
                            delayMs = it.delayMs,
                        )
                    }
                },
            )

            ModelAndView().apply {
                addObject("modifyForm", form)

                status = HttpStatus.OK
                viewName = "WebFlow/modifyForm"
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * POST: /WebFlow/{id}/Modify
     *
     * Modifies an entity.
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/WebFlow/{id}/Modify"])
    fun modify(
        @Validated modifyForm: ModifyForm,
        bindingResult: BindingResult,
    ): Any {
        if (bindingResult.hasFieldErrors("id")) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        if (bindingResult.hasErrors()) {
            return ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebFlow/modifyForm"
            }
        }

        val response: ModifyWebFlowCommand.Response = modifyWebFlowCommand.execute(
            id = UUID.fromString(modifyForm.id),
            compositions = modifyForm.compositions.map {
                ModifyWebFlowCommand.WebComposition(
                    resource = URL(it.resource),
                    widthPx = it.widthPx,
                    delayMs = it.delayMs,
                )
            },
        )

        return if (response is ModifyWebFlowCommand.Response.Ok) {
            ModelAndView().apply {
                status = HttpStatus.SEE_OTHER
                viewName = "redirect:/WebFlow/${response.webFlow.id}"
            }
        } else {
            check(response is ModifyWebFlowCommand.Response.Error)

            if (response.isNotFound) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND)
            }

            ModelAndView().apply {
                status = HttpStatus.BAD_REQUEST
                viewName = "WebFlow/createForm"
            }
        }
    }

}
