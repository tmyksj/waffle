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
    fun execute_returns_Ok() {
        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            webRegCases = listOf(
                CreateWebRegCommand.WebRegCase(
                    expected = CreateWebRegCommand.WebRegComposition(
                        resource = URL("http://127.0.0.1:8081"),
                        widthPx = 1920,
                        delayMs = 1000,
                    ),
                    actual = CreateWebRegCommand.WebRegComposition(
                        resource = URL("http://127.0.0.1:8081"),
                        widthPx = 1920,
                        delayMs = 1000,
                    ),
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
                        expected = WebReg.Composition(
                            resource = URL("http://127.0.0.1:8081"),
                            widthPx = 1920,
                            delayMs = 1000,
                        ),
                        actual = WebReg.Composition(
                            resource = URL("http://127.0.0.1:8081"),
                            widthPx = 1920,
                            delayMs = 1000,
                        ),
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
