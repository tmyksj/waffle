package waffle.test.factory

import org.springframework.boot.test.context.TestComponent
import waffle.domain.model.WebComposition
import java.net.URL

/**
 * Component for building a WebComposition model.
 */
@TestComponent
class WebCompositionFactory {

    /**
     * Returns a new model.
     */
    fun build(
        resource: URL = URL("http://127.0.0.1:8081"),
        widthPx: Long = 1920,
        delayMs: Long = 0,
    ): WebComposition {
        return WebComposition(
            resource = resource,
            widthPx = widthPx,
            delayMs = delayMs,
        )
    }

}
