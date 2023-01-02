package waffle.usecase.query

import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebRegFactory
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipOutputStream

@SpringBootTest
class FindWebRegQueryTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Autowired
    private lateinit var findWebRegQuery: FindWebRegQuery

    @Test
    fun execute_returns_Ok_when_the_WebReg_exists() {
        val result: ByteArray = ByteArrayOutputStream().apply { use { ZipOutputStream(it) } }.toByteArray()
        val entity: WebReg = webRegRepository.save(webRegFactory.build(result = result))

        val response: FindWebRegQuery.Response = findWebRegQuery.execute(
            id = entity.id,
        )

        Assertions.assertThat(response).isInstanceOf(FindWebRegQuery.Response.Ok::class.java)
        check(response is FindWebRegQuery.Response.Ok)

        SoftAssertions.assertSoftly {
            it.assertThat(response.webReg).isEqualTo(entity)
        }
    }

    @Test
    fun execute_returns_Error_when_the_WebReg_doesnt_exist() {
        val response: FindWebRegQuery.Response = findWebRegQuery.execute(
            id = UUID.randomUUID(),
        )

        Assertions.assertThat(response).isInstanceOf(FindWebRegQuery.Response.Error::class.java)
        check(response is FindWebRegQuery.Response.Error)

        SoftAssertions.assertSoftly {
            it.assertThat(response.isNotFound).isTrue
        }
    }

}
