package waffle.core.component

/**
 * Reporting component.
 */
interface ReportingComponent {

    /**
     * Compares images and creates a report compressed by zip.
     *
     * @param a images as raw bytes.
     * @param b images as raw bytes.
     * @return the report as raw bytes.
     */
    fun compareImages(a: List<ByteArray>, b: List<ByteArray>): ByteArray

}
