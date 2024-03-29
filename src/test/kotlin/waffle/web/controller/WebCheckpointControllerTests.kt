package waffle.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import waffle.core.type.Blob
import waffle.domain.entity.WebCheckpoint
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebFlowRepository
import waffle.test.factory.WebCheckpointFactory
import waffle.test.factory.WebFlowFactory
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipOutputStream

@AutoConfigureMockMvc
@SpringBootTest
class WebCheckpointControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Test
    fun createForm_responds_Ok() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun create_responds_SeeOther_when_params_are_valid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebCheckpoint")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("flow", webFlowRepository.save(webFlowFactory.build()).id.toString()),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebCheckpoint/*"))
    }

    @Test
    fun create_responds_BadRequest_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebCheckpoint")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("flow", "INVALID_ID"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun details_responds_Ok_when_the_WebCheckpoint_exists() {
        val entity: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build())

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/${entity.id}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun details_responds_NotFound_when_the_WebCheckpoint_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/${UUID.randomUUID()}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun details_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/INVALID_ID"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_Ok_when_the_WebCheckpoint_and_its_output_exist() {
        val output = Blob { ByteArrayOutputStream().apply { use { ZipOutputStream(it) } }.toByteArray().inputStream() }
        val entity: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build(output = output))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/${entity.id}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun output_responds_NotFound_when_the_output_doesnt_exist() {
        val entity: WebCheckpoint = webCheckpointRepository.save(webCheckpointFactory.build(output = null))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/${entity.id}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_NotFound_when_the_WebCheckpoint_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/${UUID.randomUUID()}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebCheckpoint/INVALID_ID/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

}
