package waffle.web.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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
            http://127.0.0.1, ,                 http://127.0.0.1, ,
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, http://127.0.0.1,""",
    )
    @ParameterizedTest
    fun create_responds_SeeOther_when_params_are_valid(
        expected0: String?,
        expected1: String?,
        actual0: String?,
        actual1: String?,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
                .apply { expected0?.let { param("cases[0].expected", it) } }
                .apply { expected1?.let { param("cases[1].expected", it) } }
                .apply { actual0?.let { param("cases[0].actual", it) } }
                .apply { actual1?.let { param("cases[1].actual", it) } },
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/WebReg/*"))
    }

    @CsvSource(
        textBlock = """
            ,                 ,                 ,                 ,    
            '',               ,                 '',               ,    
            http://127.0.0.1, ,                 ,                 ,    
            ,                 ,                 http://127.0.0.1, ,    
            http://127.0.0.1, ,                 '',               ,    
            '',               ,                 http://127.0.0.1, ,    
            http://127.0.0.1, ,                 INVALID_URL,      ,    
            INVALID_URL,      ,                 http://127.0.0.1, ,    
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, ,    
            http://127.0.0.1, ,                 http://127.0.0.1, http://127.0.0.1,
            http://127.0.0.1, http://127.0.0.1, http://127.0.0.1, '',
            http://127.0.0.1, '',               http://127.0.0.1, http://127.0.0.1,""",
    )
    @ParameterizedTest
    fun create_responds_BadRequest_when_params_are_invalid(
        expected0: String?,
        expected1: String?,
        actual0: String?,
        actual1: String?,
    ) {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/WebReg")
                .apply { expected0?.let { param("cases[0].expected", it) } }
                .apply { expected1?.let { param("cases[1].expected", it) } }
                .apply { actual0?.let { param("cases[0].actual", it) } }
                .apply { actual1?.let { param("cases[1].actual", it) } },
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

}
