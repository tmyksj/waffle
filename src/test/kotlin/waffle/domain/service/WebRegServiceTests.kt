package waffle.domain.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebRegFactory

@SpringBootTest
class WebRegServiceTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegService: WebRegService

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun run_doesnt_throw_any_exception() {
        Assertions.assertThatCode {
            webRegService.run(webRegRepository.save(webRegFactory.build()))
        }.doesNotThrowAnyException()
    }

}
