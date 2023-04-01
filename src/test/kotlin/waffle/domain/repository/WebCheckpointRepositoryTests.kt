package waffle.domain.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.domain.entity.WebCheckpoint
import waffle.domain.entity.WebFlow
import waffle.test.factory.WebCheckpointFactory
import waffle.test.factory.WebFlowFactory
import java.util.*

@SpringBootTest
class WebCheckpointRepositoryTests {

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Test
    fun crudSuccessful() {
        // Create
        val entity1: WebCheckpoint = webCheckpointFactory.build()
        val entity2: WebCheckpoint = webCheckpointRepository.save(entity1)
        Assertions.assertThat(entity2).isEqualTo(entity1)

        // Read
        val entity3: WebCheckpoint? = webCheckpointRepository.findById(UUID.randomUUID())
        Assertions.assertThat(entity3).isNull()

        val entity4: WebCheckpoint? = webCheckpointRepository.findById(entity2.id)
        Assertions.assertThat(entity4).isEqualTo(entity2)

        checkNotNull(entity4)

        // Update
        val entity5: WebCheckpoint = webCheckpointFactory.modify(entity4)
        val entity6: WebCheckpoint = webCheckpointRepository.save(entity5)
        Assertions.assertThat(entity6).isEqualTo(entity5)

        // Delete
        // is not supported.
    }

    @Test
    fun findAllByFlow_returns_entities_with_the_given_flow() {
        // Init
        val flow: WebFlow = webFlowRepository.save(webFlowFactory.build())
        val entity1: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build(flow = flow))
        val entity2: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build(flow = flow))

        // Read
        val entities: List<WebCheckpoint> = webCheckpointRepository.findAllByFlow(flow)
        Assertions.assertThat(entities).containsExactlyInAnyOrder(entity1, entity2)
    }

    @Test
    fun findAllById_returns_entities_with_the_given_ids() {
        // Init
        val entity1: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())
        val entity2: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        // Read
        val entities: List<WebCheckpoint> = webCheckpointRepository.findAllById(listOf(entity1.id, entity2.id))
        Assertions.assertThat(entities).containsExactlyInAnyOrder(entity1, entity2)
    }

}
