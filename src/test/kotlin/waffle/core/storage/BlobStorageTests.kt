package waffle.core.storage

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import waffle.core.type.Blob
import java.util.*

@SpringBootTest
class BlobStorageTests {

    @Autowired
    private lateinit var blobStorage: BlobStorage

    @Test
    fun crudSuccessful() {
        // Create
        val blob1 = Blob { "ByteArray1".toByteArray().inputStream() }
        val id1: UUID = blobStorage.save(blob1)

        // Read
        val blob2: Blob? = blobStorage.findById(UUID.randomUUID())
        Assertions.assertThat(blob2).isNull()

        val blob3: Blob? = blobStorage.findById(id1)
        Assertions.assertThat(blob3).isEqualTo(blob1)

        // Update
        // is not supported.

        // Delete
        blobStorage.deleteById(id1)

        val blob4: Blob? = blobStorage.findById(id1)
        Assertions.assertThat(blob4).isNull()
    }

    @Test
    fun findAllByID_returns_all_blobs_with_the_given_ids() {
        // Init
        val blob1 = Blob { "ByteArray1".toByteArray().inputStream() }
        val id1: UUID = blobStorage.save(blob1)

        val blob2 = Blob { "ByteArray2".toByteArray().inputStream() }
        val id2: UUID = blobStorage.save(blob2)

        // Read
        val blobs: Map<UUID, Blob> = blobStorage.findAllById(listOf(id1, id2))
        Assertions.assertThat(blobs).isEqualTo(mapOf(id1 to blob1, id2 to blob2))
    }

}
