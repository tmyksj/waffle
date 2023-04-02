package waffle.usecase.query

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.core.time.now
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.test.factory.WebCheckpointFactory
import waffle.test.factory.WebFlowFactory
import java.util.*

@SpringBootTest
class FindWebFlowQueryTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Autowired
    private lateinit var findWebFlowQuery: FindWebFlowQuery

    @Test
    fun execute_returns_Ok_when_the_WebFlow_exists() {
        val entity: WebFlow = webFlowRepository.save(webFlowFactory.build())
        val checkpoints: List<WebCheckpoint> =
            List(24) {
                when (it) {
                    in 0..5 -> {
                        webCheckpointRepository.save(
                            webCheckpointFactory.build(
                                flow = entity,
                                startedDate = now().minusHours(it.toLong()),
                                createdDate = now().minusHours(24),
                            ),
                        )
                    }

                    in 6..11 -> {
                        webCheckpointRepository.save(
                            webCheckpointFactory.build(
                                flow = entity,
                                completedDate = now().minusHours(it.toLong()),
                                createdDate = now().minusHours(24),
                            ),
                        )
                    }

                    in 12..17 -> {
                        webCheckpointRepository.save(
                            webCheckpointFactory.build(
                                flow = entity,
                                failedDate = now().minusHours(it.toLong()),
                                createdDate = now().minusHours(24),
                            ),
                        )
                    }

                    else -> {
                        webCheckpointRepository.save(
                            webCheckpointFactory.build(
                                flow = entity,
                                createdDate = now().minusHours(it.toLong()),
                            ),
                        )
                    }
                }
            }

        val response: FindWebFlowQuery.Response = findWebFlowQuery.execute(
            id = entity.id,
        )

        Assertions.assertThat(response).isInstanceOf(FindWebFlowQuery.Response.Ok::class.java)
        check(response is FindWebFlowQuery.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webFlow).isEqualTo(entity)
            it.assertThat(response.checkpoints).isEqualTo(checkpoints)
        }
    }

    @Test
    fun execute_returns_Error_when_the_WebFlow_doesnt_exist() {
        val response: FindWebFlowQuery.Response = findWebFlowQuery.execute(
            id = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(FindWebFlowQuery.Response.Error::class.java)
        check(response is FindWebFlowQuery.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}
