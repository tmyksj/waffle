package waffle.usecase.command

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebFlowRepository
import java.net.URL

@SpringBootTest
class CreateWebFlowCommandTests {

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var createWebFlowCommand: CreateWebFlowCommand

    @Test
    fun execute_returns_Ok() {
        val response: CreateWebFlowCommand.Response = createWebFlowCommand.execute(
            compositions = listOf(
                CreateWebFlowCommand.WebComposition(
                    resource = URL("http://127.0.0.1:8081"),
                    widthPx = 1920,
                    delayMs = 1000,
                ),
            ),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebFlowCommand.Response.Ok::class.java)
        check(response is CreateWebFlowCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webFlow).isEqualTo(webFlowRepository.findById(response.webFlow.id))

            it.assertThat(response.webFlow.compositions).isEqualTo(
                listOf(
                    WebComposition(
                        resource = URL("http://127.0.0.1:8081"),
                        widthPx = 1920,
                        delayMs = 1000,
                    ),
                ),
            )
        }
    }

}
