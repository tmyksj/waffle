package waffle.infrastructure.impl

import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.stereotype.Component
import waffle.component.BrowserComponent
import java.io.Closeable
import java.net.URL

@Component
class BrowserComponentImpl : BrowserComponent {

    override fun captureScreenshot(
        url: URL,
        width: Int?,
        height: Int?,
    ): ByteArray {
        return ChromeDriver(ChromeOptions().setHeadless(true)).run {
            Closeable { quit() }.use {
                manage().window().size =
                    Dimension(width ?: BrowserComponent.DEFAULT_WIDTH, height ?: BrowserComponent.DEFAULT_HEIGHT)

                get(url.toString())

                val scrollHeight: Any = executeScript("return document.body.scrollHeight;")
                check(scrollHeight is Number)

                manage().window().size =
                    Dimension(width ?: BrowserComponent.DEFAULT_WIDTH, height ?: scrollHeight.toInt())

                getScreenshotAs(OutputType.BYTES)
            }
        }
    }

}
