package waffle.domain.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebReg
import waffle.test.factory.WebRegFactory
import java.util.*

@SpringBootTest
class WebRegRepositoryTests {

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun crudSuccessful() {
        // Create
        val entity1: WebReg = webRegFactory.build()
        val entity2: WebReg = webRegRepository.save(entity1)
        Assertions.assertThat(entity2).isEqualTo(entity1)

        // Read
        val entity3: WebReg? = webRegRepository.findById(UUID.randomUUID())
        Assertions.assertThat(entity3).isNull()

        val entity4: WebReg? = webRegRepository.findById(entity2.id)
        Assertions.assertThat(entity4).isEqualTo(entity2)

        checkNotNull(entity4)

        // Update
        val entity5: WebReg = webRegFactory.modify(entity4)
        val entity6: WebReg = webRegRepository.save(entity5)
        Assertions.assertThat(entity6).isEqualTo(entity5)

        // Delete
        // is not supported.
    }

}
