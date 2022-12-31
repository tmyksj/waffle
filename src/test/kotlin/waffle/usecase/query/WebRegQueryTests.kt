package waffle.usecase.query

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebRegFactory
import java.util.*

@SpringBootTest
class WebRegQueryTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Autowired
    private lateinit var webRegQuery: WebRegQuery

    @Test
    fun details_returns_Ok_when_the_WebReg_exists() {
        val entity: WebReg = webRegRepository.save(webRegFactory.build())

        val response: WebRegQuery.DetailsResponse = webRegQuery.details(
                id = entity.id,
        )

        Assertions.assertThat(response).isInstanceOf(WebRegQuery.DetailsResponse.Ok::class.java)
        check(response is WebRegQuery.DetailsResponse.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webReg).isEqualTo(entity)
        }
    }

    @Test
    fun details_throws_Error_when_the_WebReg_doesnt_exist() {
        val response: WebRegQuery.DetailsResponse = webRegQuery.details(
                id = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(WebRegQuery.DetailsResponse.Error::class.java)
        check(response is WebRegQuery.DetailsResponse.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}
