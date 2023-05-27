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
import waffle.domain.entity.WebFlow
import waffle.domain.repository.WebFlowRepository
import waffle.test.factory.WebFlowFactory
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class WebFlowControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webFlowRepository: WebFlowRepository

    @Autowired
    private lateinit var webFlowFactory: WebFlowFactory

    @Test
    fun createForm_responds_Ok() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @CsvSource(
        textBlock = """
            http://127.0.0.1, 100,  0,     ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 4000, 60000, ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000,""",
    )
    @ParameterizedTest
    fun create_responds_SeeOther_when_params_are_valid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebFlow")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebFlow/*"))
    }

    @CsvSource(
        textBlock = """
            ,                 ,     ,      ,                 ,     ,
            '',               '',   '',    ,                 ,     ,
            INVALID_URL,      1920, 1000,  ,                 ,     ,
            http://127.0.0.1, 99,   1000,  ,                 ,     ,
            http://127.0.0.1, 4001, 1000,  ,                 ,     ,
            http://127.0.0.1, 1920, -1,    ,                 ,     ,
            http://127.0.0.1, 1920, 60001, ,                 ,     ,
            http://127.0.0.1, 1920, 1000,  '',               '',   '',
            http://127.0.0.1, 1920, 1000,  INVALID_URL,      1920, 1000,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 99,   1000,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 4001, 1000,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, -1,
            http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 60001,""",
    )
    @ParameterizedTest
    fun create_responds_BadRequest_when_params_are_invalid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebFlow")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun details_responds_Ok_when_the_WebFlow_exists() {
        val entity: WebFlow = webFlowRepository.save(webFlowFactory.build())

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/${entity.id}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun details_responds_NotFound_when_the_WebFlow_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/${UUID.randomUUID()}"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun details_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/INVALID_ID"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun modifyForm_responds_Ok_when_the_WebFlow_exists() {
        val entity: WebFlow = webFlowRepository.save(webFlowFactory.build())

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/${entity.id}/Modify"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun modifyForm_responds_NotFound_when_the_WebFlow_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/${UUID.randomUUID()}/Modify"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun modifyForm_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebFlow/INVALID_ID/Modify"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @CsvSource(
        textBlock = """
            [ID], http://127.0.0.1, 100,  0,     ,                 ,     ,
            [ID], http://127.0.0.1, 1920, 1000,  ,                 ,     ,
            [ID], http://127.0.0.1, 4000, 60000, ,                 ,     ,
            [ID], http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 1000,""",
    )
    @ParameterizedTest
    fun modify_responds_SeeOther_when_params_are_valid(
        @AggregateWith(ModifyArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        if (params.getFirst("id") == "[ID]") {
            params.set("id", webFlowRepository.save(webFlowFactory.build()).id.toString())
        }

        val id: String = checkNotNull(params.getFirst("id"))
        params.remove("id")

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebFlow/${id}/Modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebFlow/*"))
    }

    @CsvSource(
        textBlock = """
            [ID], ,                 ,     ,      ,                 ,     ,
            [ID], '',               '',   '',    ,                 ,     ,
            [ID], INVALID_URL,      1920, 1000,  ,                 ,     ,
            [ID], http://127.0.0.1, 99,   1000,  ,                 ,     ,
            [ID], http://127.0.0.1, 4001, 1000,  ,                 ,     ,
            [ID], http://127.0.0.1, 1920, -1,    ,                 ,     ,
            [ID], http://127.0.0.1, 1920, 60001, ,                 ,     ,
            [ID], http://127.0.0.1, 1920, 1000,  '',               '',   '',
            [ID], http://127.0.0.1, 1920, 1000,  INVALID_URL,      1920, 1000,
            [ID], http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 99,   1000,
            [ID], http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 4001, 1000,
            [ID], http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, -1,
            [ID], http://127.0.0.1, 1920, 1000,  http://127.0.0.1, 1920, 60001,""",
    )
    @ParameterizedTest
    fun modify_responds_BadRequest_when_params_are_invalid(
        @AggregateWith(ModifyArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        if (params.getFirst("id") == "[ID]") {
            params.set("id", webFlowRepository.save(webFlowFactory.build()).id.toString())
        }

        val id: String = checkNotNull(params.getFirst("id"))
        params.remove("id")

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebFlow/${id}/Modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @CsvSource(
        textBlock = """
            [randomUUID], http://127.0.0.1, 1920, 1000, , , ,
            INVALID_ID,   http://127.0.0.1, 1920, 1000, , , ,""",
    )
    @ParameterizedTest
    fun modify_responds_NotFound_when_params_are_invalid(
        @AggregateWith(ModifyArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        if (params.getFirst("id") == "[randomUUID]") {
            params.set("id", UUID.randomUUID().toString())
        }

        val id: String = checkNotNull(params.getFirst("id"))
        params.remove("id")

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebFlow/${id}/Modify")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    class CreateArgumentsAggregator : ArgumentsAggregator {

        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext): Any {
            return LinkedMultiValueMap(
                arrayOf(
                    "compositions[0].resource",
                    "compositions[0].widthPx",
                    "compositions[0].delayMs",
                    "compositions[1].resource",
                    "compositions[1].widthPx",
                    "compositions[1].delayMs",
                ).mapIndexed { index, s ->
                    Pair(s, accessor.getString(index))
                }.filter {
                    it.second != null
                }.groupBy({ it.first }, { it.second }),
            )
        }

    }

    class ModifyArgumentsAggregator : ArgumentsAggregator {

        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext): Any {
            return LinkedMultiValueMap(
                arrayOf(
                    "id",
                    "compositions[0].resource",
                    "compositions[0].widthPx",
                    "compositions[0].delayMs",
                    "compositions[1].resource",
                    "compositions[1].widthPx",
                    "compositions[1].delayMs",
                ).mapIndexed { index, s ->
                    Pair(s, accessor.getString(index))
                }.filter {
                    it.second != null
                }.groupBy({ it.first }, { it.second }),
            )
        }

    }

}
