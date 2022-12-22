package waffle.domain.service

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebRegFactory
import java.util.*

@SpringBootTest
class WebRegServiceTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegService: WebRegService

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun runSuccessful() {
        val id: UUID = UUID.randomUUID()

        val retVal: WebReg = webRegService.run(webRegFactory.build(id = id))
        val actual: WebReg = checkNotNull(webRegRepository.findById(id))

        SoftAssertions.assertSoftly {
            it.assertThat(actual).isEqualTo(retVal)
            it.assertThat(actual.result).isNotEmpty
        }
    }

}
