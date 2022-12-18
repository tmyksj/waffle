package waffle.component.impl

import org.springframework.stereotype.Component
import waffle.component.ContentTypeComponent
import java.net.URLConnection

@Component
class ContentTypeComponentImpl : ContentTypeComponent {

    override fun guess(byteArray: ByteArray): String? {
        return URLConnection.guessContentTypeFromStream(byteArray.inputStream())
    }

}
