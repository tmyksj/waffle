package waffle.test.factory

import org.springframework.boot.test.context.TestComponent
import waffle.domain.model.WebScreenshot
import java.net.URL

/**
 * Component for building a WebScreenshot model.
 */
@TestComponent
class WebScreenshotFactory {

    /**
     * Returns a new model.
     */
    fun build(
        resource: URL = URL("http://127.0.0.1:8081"),
        widthPx: Long = 1920,
        binary: ByteArray = checkNotNull(javaClass.getResourceAsStream("/Waffle_of_Japan_001.jpg")).readAllBytes(),
    ): WebScreenshot {
        return WebScreenshot(
            resource = resource,
            widthPx = widthPx,
            binary = binary,
        )
    }

}
