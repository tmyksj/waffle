package waffle.core.storage

import waffle.core.type.Blob
import java.util.*

/**
 * Storage for a binary large object.
 */
interface BlobStorage {

    /**
     * Deletes the blob with the given id.
     *
     * @param id
     */
    fun deleteById(id: UUID)

    /**
     * Returns all blobs with the given ids.
     *
     * @param ids
     * @return the blobs with the given ids.
     */
    fun findAllById(ids: Iterable<UUID>): Map<UUID, Blob>

    /**
     * Retrieves a blob by its id.
     *
     * @param id
     * @return the blob with the given id or null if none found.
     */
    fun findById(id: UUID): Blob?

    /**
     * Saves a given blob.
     *
     * @param blob
     * @return a generated id.
     */
    fun save(blob: Blob): UUID

}
