package waffle.domain.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebInstant
import waffle.test.factory.WebInstantFactory
import java.util.*

@SpringBootTest
class WebInstantRepositoryTests {

    @Autowired
    private lateinit var webInstantRepository: WebInstantRepository

    @Autowired
    private lateinit var webInstantFactory: WebInstantFactory

    @Test
    fun crudSuccessful() {
        // Create
        val entity1: WebInstant = webInstantFactory.build()
        val entity2: WebInstant = webInstantRepository.save(entity1)
        Assertions.assertThat(entity2).isEqualTo(entity1)

        // Read
        val entity3: WebInstant? = webInstantRepository.findById(UUID.randomUUID())
        Assertions.assertThat(entity3).isNull()

        val entity4: WebInstant? = webInstantRepository.findById(entity2.id)
        Assertions.assertThat(entity4).isEqualTo(entity2)

        checkNotNull(entity4)

        // Update
        val entity5: WebInstant = webInstantFactory.modify(entity4)
        val entity6: WebInstant = webInstantRepository.save(entity5)
        Assertions.assertThat(entity6).isEqualTo(entity5)

        // Delete
        // is not supported.
    }

}
