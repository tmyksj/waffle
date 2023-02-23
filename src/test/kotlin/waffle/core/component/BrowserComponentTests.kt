package waffle.core.component

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.net.URL

@SpringBootTest
class BrowserComponentTests {

    @Autowired
    private lateinit var browserComponent: BrowserComponent

    @Test
    fun captureScreenshot_returns_bytes() {
        val actual: ByteArray = browserComponent.captureScreenshot(URL("http://127.0.0.1:8081/index.html"))
        Assertions.assertThat(actual.size).isNotZero
    }

}
