package waffle.component

import java.net.URL

/**
 * Web browser component.
 */
interface BrowserComponent {

    companion object {

        /**
         * Default width.
         */
        const val DEFAULT_WIDTH: Int = 1920

        /**
         * Default height.
         */
        const val DEFAULT_HEIGHT: Int = 1080

    }

    /**
     * Captures a screenshot as raw bytes.
     *
     * @param url
     * @param width
     * @param height
     * @return the captured screenshot.
     */
    fun captureScreenshot(
        url: URL,
        width: Int? = null,
        height: Int? = null,
    ): ByteArray

}
