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
     * Renders a homepage.
     */
    @RequestMapping(method = [RequestMethod.GET], path = ["/Home"])
    fun index(): Any {
        return ModelAndView().apply {
            status = HttpStatus.OK
            viewName = "Home/index"
        }
    }

}
