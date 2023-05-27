package waffle.web.validation.impl

import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.validation.DataBinder
import org.springframework.validation.Validator
import waffle.web.validation.ValidationComponent

@Component
class ValidationComponentImpl(
    private val validator: Validator,
) : ValidationComponent {

    override fun validate(target: Any): BindingResult {
        val dataBinder = DataBinder(target)
        dataBinder.validator = validator
        dataBinder.validate()

        return dataBinder.bindingResult
    }

}
