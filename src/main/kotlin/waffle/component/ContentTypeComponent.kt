package waffle.component

/**
 * Content type component.
 */
interface ContentTypeComponent {

    /**
     * Tries to determine the type of bytes.
     * @param byteArray
     * @return a guess at the content type, or null if none can be determined.
     */
    fun guess(byteArray: ByteArray): String?

}
