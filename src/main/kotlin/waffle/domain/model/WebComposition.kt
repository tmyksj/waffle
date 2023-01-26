package waffle.domain.model

import java.net.URL

/**
 * Composition for a page.
 */
data class WebComposition(

    /**
     * URL for a page.
     */
    val resource: URL,

    /**
     * Window width for a page (pixels).
     */
    val widthPx: Long = 1920,

    /**
     * Waiting time before accessing to a page (milliseconds).
     */
    val delayMs: Long = 0,

    ) {

    init {
        // widthPx must be in range from 100 to 4000.
        if (widthPx !in 100..4000) {
            throw IllegalArgumentException()
        }

        // delayMs must be in range from zero to 1 minute.
        if (delayMs !in 0..60000) {
            throw IllegalArgumentException()
        }
    }

}
