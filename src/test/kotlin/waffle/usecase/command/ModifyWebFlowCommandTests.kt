package waffle.usecase.command

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebFlow
import waffle.domain.model.WebComposition
import waffle.domain.repository.WebFlowRepository
import waffle.test.factory.WebCompositionFactory
import waffle.test.factory.WebFlowFactory
import java.net.URL
import java.util.*

@SpringBootTest
class ModifyWebFlowCommandTests {

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webCompositionFactory: WebCompositionFactory

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Autowired
    private lateinit var modifyWebFlowCommand: ModifyWebFlowCommand

    @Test
    fun execute_returns_Ok_when_the_WebFlow_exists() {
        val entity: WebFlow = webFlowRepository.save(webFlowFactory.build())
        val compositions: List<WebComposition> =
            listOf(
                webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
                webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
            )

        val response: ModifyWebFlowCommand.Response = modifyWebFlowCommand.execute(
            id = entity.id,
            compositions = compositions.map {
                ModifyWebFlowCommand.WebComposition(
                    resource = it.resource,
                    widthPx = it.widthPx,
                    heightPx = it.heightPx,
                    delayMs = it.delayMs,
                )
            },
        )

        Assertions.assertThat(response).isInstanceOf(ModifyWebFlowCommand.Response.Ok::class.java)
        check(response is ModifyWebFlowCommand.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webFlow).isEqualTo(
                webFlowFactory.modify(
                    entity,
                    compositions = compositions,
                    lastModifiedDate = response.webFlow.lastModifiedDate,
                ),
            )
        }
    }

    @Test
    fun execute_returns_Error_when_the_WebFlow_doesnt_exist() {
        val response: ModifyWebFlowCommand.Response = modifyWebFlowCommand.execute(
            id = UUID.randomUUID(),
            compositions = listOf(),
        )

        Assertions.assertThat(response).isInstanceOf(ModifyWebFlowCommand.Response.Error::class.java)
        check(response is ModifyWebFlowCommand.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}
