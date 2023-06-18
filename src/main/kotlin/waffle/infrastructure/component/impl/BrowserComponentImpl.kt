package waffle.infrastructure.component.impl

import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.stereotype.Component
import waffle.core.component.BrowserComponent
import java.io.Closeable
import java.net.URL
import java.util.*

@Component
class BrowserComponentImpl : BrowserComponent {

    override fun captureScreenshot(
        url: URL,
        width: Int,
        height: Int,
    ): ByteArray {
        val options: ChromeOptions = ChromeOptions()
            .addArguments("--headless")
            .addArguments("--hide-scrollbars")

        return ChromeDriver(options).run {
            Closeable { quit() }.use {
                manage().window().size = Dimension(width, height)

                get(url.toString())

                // https://chromedevtools.github.io/devtools-protocol/tot/Page/#method-captureScreenshot
                val response: Map<String, Any> =
                    executeCdpCommand("Page.captureScreenshot", mapOf("captureBeyondViewport" to true))

                val data: Any? = response["data"]
                check(data is String)

                Base64.getDecoder().decode(data)
            }
        }
    }

}
