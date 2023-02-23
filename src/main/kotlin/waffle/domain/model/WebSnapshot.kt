package waffle.domain.model

import waffle.core.type.Blob
import java.net.URL

/**
 * Snapshot of a page.
 */
data class WebSnapshot(

    /**
     * URL for a page.
     */
    val resource: URL,

    /**
     * Window width for a page (pixels).
     */
    val widthPx: Long = 1920,

    /**
     * Screenshot of a page.
     */
    val screenshot: Blob = Blob(),

    ) {

    init {
        // widthPx must be in range from 100 to 4000.
        if (widthPx !in 100..4000) {
            throw IllegalArgumentException()
        }
    }

}
