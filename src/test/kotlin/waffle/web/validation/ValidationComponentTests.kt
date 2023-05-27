package waffle.web.validation

import jakarta.validation.constraints.Pattern
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.validation.BindingResult
import java.util.*

@SpringBootTest
class ValidationComponentTests {

    @Autowired
    private lateinit var validationComponent: ValidationComponent

    @Test
    fun validate_returns_BindingResult_that_has_no_errors_when_the_given_target_is_valid() {
        val actual: BindingResult = validationComponent.validate(
            MockForm(
                id = UUID.randomUUID().toString(),
            ),
        )

        Assertions.assertThat(actual.hasErrors()).isFalse
    }

    @Test
    fun validate_returns_BindingResult_that_has_errors_when_the_given_target_is_invalid() {
        val actual: BindingResult = validationComponent.validate(
            MockForm(
                id = "INVALID_ID",
            ),
        )

        Assertions.assertThat(actual.hasErrors()).isTrue
    }

    data class MockForm(

        @field:Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
        val id: String = "",

        )

}
