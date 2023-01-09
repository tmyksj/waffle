package waffle.infrastructure.impl

import org.apache.tika.Tika
import org.springframework.stereotype.Component
import waffle.component.ContentTypeComponent

@Component
class ContentTypeComponentImpl : ContentTypeComponent {

    override fun guess(byteArray: ByteArray): String? {
        return Tika().detect(byteArray)
    }

}
