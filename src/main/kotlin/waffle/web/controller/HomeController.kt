package waffle.web.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

/**
 * Controller for homepage.
 */
@Controller
class HomeController {

    /**
     * GET: /Home
     *
     * Redirects to `/WebReg`.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/Home"])
    fun index(): Any {
        return ModelAndView().apply {
            status = HttpStatus.SEE_OTHER
            viewName = "redirect:/WebReg"
        }
    }

}
