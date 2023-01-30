package waffle.domain.model

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
    val screenshot: ByteArray = ByteArray(0),

    ) {

    init {
        // widthPx must be in range from 100 to 4000.
        if (widthPx !in 100..4000) {
            throw IllegalArgumentException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebSnapshot

        if (resource != other.resource) return false
        if (widthPx != other.widthPx) return false
        if (!screenshot.contentEquals(other.screenshot)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resource.hashCode()
        result = 31 * result + widthPx.hashCode()
        result = 31 * result + screenshot.contentHashCode()
        return result
    }

}
