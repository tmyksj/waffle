package waffle.domain.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.repository.WebCheckpointRepository
import waffle.test.factory.WebCheckpointFactory

@SpringBootTest
class WebCheckpointServiceTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webCheckpointService: WebCheckpointService

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Test
    fun create_doesnt_throw_any_exception() {
        Assertions.assertThatCode {
            webCheckpointService.create(webCheckpointRepository.save(webCheckpointFactory.build()))
        }.doesNotThrowAnyException()
    }

}
