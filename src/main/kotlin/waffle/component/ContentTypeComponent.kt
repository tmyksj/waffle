package waffle.component

/**
 * Content type component.
 */
interface ContentTypeComponent {

    /**
     * Tries to determine the extension of bytes.
     * @param byteArray
     * @return a guess at the extension, or empty string if none can be determined.
     */
    fun guessExtension(byteArray: ByteArray): String

    /**
     * Tries to determine the type of bytes.
     * @param byteArray
     * @return a guess at the type, or application/octet-stream if none can be determined.
     */
    fun guessType(byteArray: ByteArray): String

}
