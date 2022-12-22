package waffle.domain.entity

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.test.factory.WebRegFactory

@SpringBootTest
class WebRegTests {

    @Autowired
    private lateinit var webRegFactory: WebRegFactory

    @Test
    fun transition_to_the_state_Started() {
        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.start()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebReg.State.Started)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Completed() {
        val result = ByteArray(0)

        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.complete(result)

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebReg.State.Completed)
            it.assertThat(actual.result).isEqualTo(result)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

    @Test
    fun transition_to_the_state_Failed() {
        val entity: WebReg = webRegFactory.build()
        val actual: WebReg = entity.fail()

        SoftAssertions.assertSoftly {
            it.assertThat(actual.state).isEqualTo(WebReg.State.Failed)
            it.assertThat(actual.lastModifiedDate).isAfter(entity.lastModifiedDate)
        }
    }

}
