package waffle.infrastructure.impl

import org.apache.tika.Tika
import org.apache.tika.mime.MimeTypes
import org.springframework.stereotype.Component
import waffle.component.ContentTypeComponent

@Component
class ContentTypeComponentImpl : ContentTypeComponent {

    override fun guessExtension(byteArray: ByteArray): String {
        return MimeTypes.getDefaultMimeTypes().forName(guessType(byteArray)).extension
    }

    override fun guessType(byteArray: ByteArray): String {
        return Tika().detect(byteArray)
    }

}
