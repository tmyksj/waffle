package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for root.
 */
@Controller
class RootController {

    /**
     * GET: /
     *
     * Redirects to `/Home`.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/"])
    fun index(): Any {
        return ModelAndView().apply {
            status = HttpStatus.SEE_OTHER
            viewName = "redirect:/Home"
        }
    }

}
