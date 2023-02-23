package waffle.domain.type

import java.io.InputStream

/**
 * Binary large object.
 */
data class Blob(

    /**
     * Reads a binary with lazy loading.
     */
    private val initializer: () -> InputStream = { ByteArray(0).inputStream() },

    ) {

    /**
     * ByteArray.
     */
    val byteArray: ByteArray by lazy {
        use {
            it.readBytes()
        }
    }

    /**
     * Executes the given [block] function on this object.
     *
     * @param block a function to process this object.
     * @return the result of [block] function invoked on this object.
     */
    fun <R> use(block: (InputStream) -> R): R {
        return initializer().use(block)
    }

    override fun equals(other: Any?): Boolean {
        return other is Blob && byteArray.contentEquals(other.byteArray)
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }

}
