package waffle.web.validation

import org.springframework.validation.BindingResult

/**
 * Validation component.
 */
interface ValidationComponent {

    /**
     * Validates the given target and returns its result.
     *
     * @param target
     * @return
     */
    fun validate(target: Any): BindingResult

}
