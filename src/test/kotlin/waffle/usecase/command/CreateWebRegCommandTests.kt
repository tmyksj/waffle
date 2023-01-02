package waffle.usecase.command

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import java.net.URL

@SpringBootTest
class CreateWebRegCommandTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var createWebRegCommand: CreateWebRegCommand

    @Test
    fun execute_responds_Ok() {
        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
                webRegCases = listOf(
                        CreateWebRegCommand.WebRegCase(
                                expected = URL("http://127.0.0.1:8081"),
                                actual = URL("http://127.0.0.1:8081"),
                        ),
                ),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Ok::class.java)
        check(response is CreateWebRegCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webReg).isEqualTo(webRegRepository.findById(response.webReg.id))

            it.assertThat(response.webReg.cases).isEqualTo(
                    listOf(
                            WebReg.Case(
                                    expected = URL("http://127.0.0.1:8081"),
                                    actual = URL("http://127.0.0.1:8081"),
                            ),
                    ),
            )
            it.assertThat(response.webReg.result).isNull()
            it.assertThat(response.webReg.state).isEqualTo(WebReg.State.Ready)
            it.assertThat(response.webReg.startedDate).isNull()
            it.assertThat(response.webReg.completedDate).isNull()
            it.assertThat(response.webReg.failedDate).isNull()
        }
    }

}
