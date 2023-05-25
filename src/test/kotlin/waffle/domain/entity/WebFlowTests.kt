package waffle.domain.entity

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.model.WebComposition
import waffle.test.factory.WebCompositionFactory
import waffle.test.factory.WebFlowFactory
import java.net.URL
import java.util.*

@SpringBootTest
class WebFlowTests {

    @Autowired
    private lateinit var webCompositionFactory: WebCompositionFactory

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Test
    fun recompose() {
        val compositions1: List<WebComposition> = listOf(
            webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
            webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
        )

        val compositions2: List<WebComposition> = listOf(
            webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
            webCompositionFactory.build(URL("http://127.0.0.1:8081/${UUID.randomUUID()}")),
        )

        val entity: WebFlow = webFlowFactory.build(compositions = compositions1)
        val actual: WebFlow = entity.recompose(compositions2)

        SoftAssertions.assertSoftly {
            it.assertThat(actual.compositions).isEqualTo(compositions2)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

}
