package waffle.usecase.command

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.test.factory.WebFlowFactory
import java.util.*

@SpringBootTest
class CreateWebCheckpointCommandTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Autowired
    private lateinit var createWebCheckpointCommand: CreateWebCheckpointCommand

    @Test
    fun execute_returns_Ok_when_the_WebFlow_exists() {
        val flow: WebFlow = webFlowRepository.save(webFlowFactory.build())

        val response: CreateWebCheckpointCommand.Response = createWebCheckpointCommand.execute(
            flow = flow.id,
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebCheckpointCommand.Response.Ok::class.java)
        check(response is CreateWebCheckpointCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webCheckpoint).isEqualTo(webCheckpointRepository.findById(response.webCheckpoint.id))

            it.assertThat(response.webCheckpoint.flow).isEqualTo(flow)
            it.assertThat(response.webCheckpoint.snapshots).isEqualTo(listOf<WebSnapshot>())
            it.assertThat(response.webCheckpoint.state).isEqualTo(WebCheckpoint.State.Ready)
            it.assertThat(response.webCheckpoint.startedDate).isNull()
            it.assertThat(response.webCheckpoint.completedDate).isNull()
            it.assertThat(response.webCheckpoint.failedDate).isNull()
        }
    }

    @Test
    fun execute_returns_Error_when_the_WebFlow_doesnt_exist() {
        val response: CreateWebCheckpointCommand.Response = createWebCheckpointCommand.execute(
            flow = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebCheckpointCommand.Response.Error::class.java)
        check(response is CreateWebCheckpointCommand.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}
