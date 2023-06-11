package waffle.usecase.command

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebReg
import waffle.domain.model.WebComposition
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebCheckpointFactory
import java.net.URL
import java.util.*

@SpringBootTest
class CreateWebRegCommandTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var createWebRegCommand: CreateWebRegCommand

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Test
    fun execute_returns_Ok_when_the_checkpoints_exist() {
        val checkpointA: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())
        val checkpointB: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = checkpointA.id,
            checkpointB = checkpointB.id,
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Ok::class.java)
        check(response is CreateWebRegCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webReg).isEqualTo(webRegRepository.findById(response.webReg.id))

            it.assertThat(response.webReg.checkpointA).isEqualTo(checkpointA)
            it.assertThat(response.webReg.checkpointB).isEqualTo(checkpointB)

            it.assertThat(response.webReg.output).isNull()
            it.assertThat(response.webReg.state).isEqualTo(WebReg.State.Ready)
            it.assertThat(response.webReg.startedDate).isNull()
            it.assertThat(response.webReg.completedDate).isNull()
            it.assertThat(response.webReg.failedDate).isNull()
        }
    }

    @Test
    fun execute_returns_Ok_when_compositions_are_provided() {
        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = listOf(
                CreateWebRegCommand.WebComposition(
                    resource = URL("http://127.0.0.1:8081"),
                    widthPx = 1920,
                    heightPx = 1080,
                    delayMs = 1000,
                ),
            ),
            checkpointB = listOf(
                CreateWebRegCommand.WebComposition(
                    resource = URL("http://127.0.0.1:8081"),
                    widthPx = 1920,
                    heightPx = 1080,
                    delayMs = 1000,
                ),
            ),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Ok::class.java)
        check(response is CreateWebRegCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webReg).isEqualTo(webRegRepository.findById(response.webReg.id))

            it.assertThat(response.webReg.checkpointA.flow.compositions).isEqualTo(
                listOf(
                    WebComposition(
                        resource = URL("http://127.0.0.1:8081"),
                        widthPx = 1920,
                        heightPx = 1080,
                        delayMs = 1000,
                    ),
                ),
            )
            it.assertThat(response.webReg.checkpointA.snapshots).isEqualTo(listOf<WebSnapshot>())
            it.assertThat(response.webReg.checkpointA.state).isEqualTo(WebCheckpoint.State.Ready)
            it.assertThat(response.webReg.checkpointA.startedDate).isNull()
            it.assertThat(response.webReg.checkpointA.completedDate).isNull()
            it.assertThat(response.webReg.checkpointA.failedDate).isNull()

            it.assertThat(response.webReg.checkpointB.flow.compositions).isEqualTo(
                listOf(
                    WebComposition(
                        resource = URL("http://127.0.0.1:8081"),
                        widthPx = 1920,
                        heightPx = 1080,
                        delayMs = 1000,
                    ),
                ),
            )
            it.assertThat(response.webReg.checkpointB.snapshots).isEqualTo(listOf<WebSnapshot>())
            it.assertThat(response.webReg.checkpointB.state).isEqualTo(WebCheckpoint.State.Ready)
            it.assertThat(response.webReg.checkpointB.startedDate).isNull()
            it.assertThat(response.webReg.checkpointB.completedDate).isNull()
            it.assertThat(response.webReg.checkpointB.failedDate).isNull()

            it.assertThat(response.webReg.output).isNull()
            it.assertThat(response.webReg.state).isEqualTo(WebReg.State.Ready)
            it.assertThat(response.webReg.startedDate).isNull()
            it.assertThat(response.webReg.completedDate).isNull()
            it.assertThat(response.webReg.failedDate).isNull()
        }
    }

    @Test
    fun execute_returns_Error_when_the_checkpointA_doesnt_exist() {
        val checkpointB: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = UUID.randomUUID(),
            checkpointB = checkpointB.id,
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Error::class.java)
        check(response is CreateWebRegCommand.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFoundCheckpointA).isTrue
            it.assertThat(response.isNotFoundCheckpointB).isFalse
        }
    }

    @Test
    fun execute_returns_Error_when_the_checkpointB_doesnt_exist() {
        val checkpointA: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = checkpointA.id,
            checkpointB = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Error::class.java)
        check(response is CreateWebRegCommand.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFoundCheckpointA).isFalse
            it.assertThat(response.isNotFoundCheckpointB).isTrue
        }
    }

    @Test
    fun execute_returns_Error_when_the_checkpoints_dont_exist() {
        val response: CreateWebRegCommand.Response = createWebRegCommand.execute(
            checkpointA = UUID.randomUUID(),
            checkpointB = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(CreateWebRegCommand.Response.Error::class.java)
        check(response is CreateWebRegCommand.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFoundCheckpointA).isTrue
            it.assertThat(response.isNotFoundCheckpointB).isTrue
        }
    }

}
