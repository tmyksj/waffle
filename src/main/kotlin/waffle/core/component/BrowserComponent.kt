package waffle.core.component

import java.net.URL

/**
 * Web browser component.
 */
interface BrowserComponent {

    /**
     * Captures a screenshot as raw bytes.
     *
     * @param url
     * @param width width of the window.
     * @param height height of the window.
     * @return the captured screenshot.
     */
    fun captureScreenshot(
        url: URL,
        width: Int = 1920,
        height: Int = 1080,
    ): ByteArray

}
