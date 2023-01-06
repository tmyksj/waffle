package waffle.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class HomeControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun index_responds_SeeOther() {
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/Home"),
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther)
            .andExpect(MockMvcResultMatchers.redirectedUrl("/WebReg"))
    }

}
