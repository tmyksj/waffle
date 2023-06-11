package waffle.test.factory

import org.springframework.boot.test.context.TestComponent
import waffle.core.type.Blob
import waffle.domain.model.WebSnapshot
import java.net.URL

/**
 * Component for building a WebSnapshot model.
 */
@TestComponent
class WebSnapshotFactory {

    /**
     * Returns a new model.
     */
    fun build(
        resource: URL = URL("http://127.0.0.1:8081"),
        widthPx: Long = 1920,
        heightPx: Long = 1080,
        screenshot: Blob = Blob {
            checkNotNull(javaClass.getResourceAsStream("/Waffle_of_Japan_001.jpg"))
        },
    ): WebSnapshot {
        return WebSnapshot(
            resource = resource,
            widthPx = widthPx,
            heightPx = heightPx,
            screenshot = screenshot,
        )
    }

}
