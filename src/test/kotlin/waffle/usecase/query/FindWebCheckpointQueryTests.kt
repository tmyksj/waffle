package waffle.usecase.query

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebCheckpoint
import waffle.domain.repository.WebCheckpointRepository
import waffle.test.factory.WebCheckpointFactory
import java.util.*

@SpringBootTest
class FindWebCheckpointQueryTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var findWebCheckpointQuery: FindWebCheckpointQuery

    @Test
    fun execute_returns_Ok_when_the_WebCheckpoint_exists() {
        val entity: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        val response: FindWebCheckpointQuery.Response = findWebCheckpointQuery.execute(
            id = entity.id,
        )

        Assertions.assertThat(response).isInstanceOf(FindWebCheckpointQuery.Response.Ok::class.java)
        check(response is FindWebCheckpointQuery.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webCheckpoint).isEqualTo(entity)
        }
    }

    @Test
    fun execute_returns_Error_when_the_WebCheckpoint_doesnt_exist() {
        val response: FindWebCheckpointQuery.Response = findWebCheckpointQuery.execute(
            id = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(FindWebCheckpointQuery.Response.Error::class.java)
        check(response is FindWebCheckpointQuery.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}