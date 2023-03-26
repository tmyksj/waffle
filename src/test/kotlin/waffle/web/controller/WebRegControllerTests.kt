package waffle.web.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import waffle.core.type.Blob
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.repository.WebRegRepository
import waffle.test.factory.WebCheckpointFactory
import waffle.test.factory.WebRegFactory
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipOutputStream

@AutoConfigureMockMvc
@SpringBootTest
class WebRegControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webCheckpointRepository: WebCheckpointRepository

    @Autowired
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webCheckpointFactory: WebCheckpointFactory

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun createForm_responds_Ok() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @CsvSource(
        textBlock = """
            [ID], [ID],""",
    )
    @ParameterizedTest
    fun create_responds_SeeOther_when_params_are_valid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        if (params.getFirst("checkpointA") == "[ID]") {
            params.set("checkpointA", webCheckpointRepository.save(webCheckpointFactory.build()).id.toString())
        }

        if (params.getFirst("checkpointB") == "[ID]") {
            params.set("checkpointB", webCheckpointRepository.save(webCheckpointFactory.build()).id.toString())
        }

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebReg/*"))
    }

    @CsvSource(
        textBlock = """
            ,           ,
            '',         '',
            [ID],       ,
            [ID],       '',
            ,           [ID],
            '',         [ID],
            INVALID_ID, [ID],
            [ID],       INVALID_ID,""",
    )
    @ParameterizedTest
    fun create_responds_BadRequest_when_params_are_invalid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        if (params.getFirst("checkpointA") == "[ID]") {
            params.set("checkpointA", webCheckpointRepository.save(webCheckpointFactory.build()).id.toString())
        }

        if (params.getFirst("checkpointB") == "[ID]") {
            params.set("checkpointB", webCheckpointRepository.save(webCheckpointFactory.build()).id.toString())
        }

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun quickstartForm_responds_Ok() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/Quickstart"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @CsvSource(
        textBlock = """
            http://127.0.0.1, 100,  0,     ,                 ,     ,     http://127.0.0.1, 100,  0,     ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 4000, 60000, ,                 ,     ,     http://127.0.0.1, 4000, 60000, ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000, http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000, http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000,""",
    )
    @ParameterizedTest
    fun quickstart_responds_SeeOther_when_params_are_valid(
        @AggregateWith(QuickstartArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg/Quickstart")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebReg/*"))
    }

    @CsvSource(
        textBlock = """
            ,                 ,     ,      ,                 ,     ,     ,                 ,     ,      ,                 ,     ,
            '',               '',   '',    ,                 ,     ,     '',               '',   '',    ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     ,                 ,     ,      ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     '',               '',   '',    ,                 ,     ,
            ,                 ,     ,      ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            '',               '',   '',    ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            INVALID_URL,      1920, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     INVALID_URL,      1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 99,   1000,  ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 99,   1000,  ,                 ,     ,
            http://127.0.0.1, 4001, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 4001, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, -1,    ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, -1,    ,                 ,     ,
            http://127.0.0.1, 1920, 60001, ,                 ,     ,     http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,     http://127.0.0.1, 1920, 60001, ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000, http://127.0.0.1, 1920, 1000,  '',               '',   '',
            http://127.0.0.1, 1920, 1000,  '',               '',   '',   http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000,""",
    )
    @ParameterizedTest
    fun quickstart_responds_BadRequest_when_params_are_invalid(
        @AggregateWith(QuickstartArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg/Quickstart")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun details_responds_Ok_when_the_WebReg_exists() {
        val entity: WebReg = webRegRepository.save(webRegFactory.build())

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${entity.id}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun details_responds_NotFound_when_the_WebReg_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${UUID.randomUUID()}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun details_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/INVALID_ID"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_Ok_when_the_WebReg_and_its_output_exist() {
        val output = Blob { ByteArrayOutputStream().apply { use { ZipOutputStream(it) } }.toByteArray().inputStream() }
        val entity: WebReg = webRegRepository.save(webRegFactory.build(output = output))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${entity.id}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun output_responds_NotFound_when_the_output_doesnt_exist() {
        val entity: WebReg = webRegRepository.save(webRegFactory.build(output = null))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${entity.id}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_NotFound_when_the_WebReg_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${UUID.randomUUID()}/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun output_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/INVALID_ID/Output"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    class CreateArgumentsAggregator : ArgumentsAggregator {

        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext): Any {
            return LinkedMultiValueMap(
                arrayOf(
                    "checkpointA",
                    "checkpointB",
                ).mapIndexed { index, s ->
                    Pair(s, accessor.getString(index))
                }.filter {
                    it.second != null
                }.groupBy({ it.first }, { it.second }),
            )
        }

    }

    class QuickstartArgumentsAggregator : ArgumentsAggregator {

        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext): Any {
            return LinkedMultiValueMap(
                arrayOf(
                    "checkpointA[0].resource",
                    "checkpointA[0].widthPx",
                    "checkpointA[0].delayMs",
                    "checkpointA[1].resource",
                    "checkpointA[1].widthPx",
                    "checkpointA[1].delayMs",
                    "checkpointB[0].resource",
                    "checkpointB[0].widthPx",
                    "checkpointB[0].delayMs",
                    "checkpointB[1].resource",
                    "checkpointB[1].widthPx",
                    "checkpointB[1].delayMs",
                ).mapIndexed { index, s ->
                    Pair(s, accessor.getString(index))
                }.filter {
                    it.second != null
                }.groupBy({ it.first }, { it.second }),
            )
        }

    }

}
