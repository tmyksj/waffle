package waffle.domain.type

/**
 * A binary large object.
 */
data class Blob(
    private val initializer: () -> ByteArray = { ByteArray(0) },
) {

    /**
     * ByteArray.
     */
    val byteArray: ByteArray by lazy(initializer)

    override fun equals(other: Any?): Boolean {
        return other is Blob && byteArray.contentEquals(other.byteArray)
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }

}
