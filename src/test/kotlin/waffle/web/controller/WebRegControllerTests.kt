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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import waffle.domain.entity.WebReg
import waffle.domain.repository.WebRegRepository
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
    private lateinit var webRegRepository: WebRegRepository

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun index_responds_Ok() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @CsvSource(
        textBlock = """
            http://127.0.0.1, http://127.0.0.1, ,                 ,
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, http://127.0.0.1,""",
    )
    @ParameterizedTest
    fun create_responds_SeeOther_when_params_are_valid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
                .params(params),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebReg/*"))
    }

    @CsvSource(
        textBlock = """
            ,                 ,                 ,                 ,
            '',               '',               ,                 ,
            http://127.0.0.1, ,                 ,                 ,
            ,                 http://127.0.0.1, ,                 ,
            http://127.0.0.1, '',               ,                 ,
            '',               http://127.0.0.1, ,                 ,
            http://127.0.0.1, INVALID_URL,      ,                 ,
            INVALID_URL,      http://127.0.0.1, ,                 ,
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, ,
            http://127.0.0.1, http://127.0.0.1, ,                 http://127.0.0.1,
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, '',
            http://127.0.0.1, http://127.0.0.1, '',               http://127.0.0.1,""",
    )
    @ParameterizedTest
    fun create_responds_BadRequest_when_params_are_invalid(
        @AggregateWith(CreateArgumentsAggregator::class) params: MultiValueMap<String, String>,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
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
    fun result_responds_Ok_when_the_WebReg_and_its_result_exist() {
        val result: ByteArray = ByteArrayOutputStream().apply { use { ZipOutputStream(it) } }.toByteArray()
        val entity: WebReg = webRegRepository.save(webRegFactory.build(result = result))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${entity.id}/Result"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun result_responds_NotFound_when_the_result_doesnt_exist() {
        val entity: WebReg = webRegRepository.save(webRegFactory.build(result = null))

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${entity.id}/Result"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun result_responds_NotFound_when_the_WebReg_doesnt_exist() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/${UUID.randomUUID()}/Result"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun result_responds_NotFound_when_params_are_invalid() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/WebReg/INVALID_ID/Result"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    class CreateArgumentsAggregator : ArgumentsAggregator {

        override fun aggregateArguments(accessor: ArgumentsAccessor, context: ParameterContext): Any {
            return LinkedMultiValueMap(
                arrayOf(
                    "cases[0].expected.resource",
                    "cases[0].actual.resource",
                    "cases[1].expected.resource",
                    "cases[1].actual.resource",
                ).mapIndexed { index, s ->
                    Pair(s, accessor.getString(index))
                }.filter {
                    it.second != null
                }.groupBy({ it.first }, { it.second }),
            )
        }

    }

}
