package waffle.domain.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebFlow
import waffle.test.factory.WebFlowFactory
import java.util.*

@SpringBootTest
class WebFlowRepositoryTests {

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Test
    fun crudSuccessful() {
        // Create
        val entity1: WebFlow = webFlowFactory.build()
        val entity2: WebFlow = webFlowRepository.save(entity1)
        Assertions.assertThat(entity2).isEqualTo(entity1)

        // Read
        val entity3: WebFlow? = webFlowRepository.findById(UUID.randomUUID())
        Assertions.assertThat(entity3).isNull()

        val entity4: WebFlow? = webFlowRepository.findById(entity2.id)
        Assertions.assertThat(entity4).isEqualTo(entity2)

        checkNotNull(entity4)

        // Update
        val entity5: WebFlow = webFlowFactory.modify(entity4)
        val entity6: WebFlow = webFlowRepository.save(entity5)
        Assertions.assertThat(entity6).isEqualTo(entity5)

        // Delete
        // is not supported.
    }

}
