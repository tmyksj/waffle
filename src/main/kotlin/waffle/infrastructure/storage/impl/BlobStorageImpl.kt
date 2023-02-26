package waffle.infrastructure.storage.impl

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.core.storage.BlobStorage
import waffle.core.type.Blob
import waffle.infrastructure.jpa.entity.BlobJpaEntity
import waffle.infrastructure.jpa.entity.BlobValueJpaEntity
import waffle.infrastructure.jpa.repository.BlobJpaRepository
import waffle.infrastructure.jpa.repository.BlobValueJpaRepository
import java.util.*

@Component
@Transactional
class BlobStorageImpl(
    private val blobJpaRepository: BlobJpaRepository,
    private val blobValueJpaRepository: BlobValueJpaRepository,
) : BlobStorage {

    override fun deleteById(id: UUID) {
        blobJpaRepository.deleteById(id.toString())
    }

    override fun findAllById(ids: Iterable<UUID>): Map<UUID, Blob> {
        return mutableMapOf<UUID, Blob>().apply {
            blobJpaRepository.findAllById(ids.map { it.toString() }).forEach {
                this[UUID.fromString(it.id)] = Blob { blobValueJpaRepository.findById(it.id).get().value.inputStream() }
            }
        }
    }

    override fun findById(id: UUID): Blob? {
        return if (blobJpaRepository.existsById(id.toString())) {
            Blob { blobValueJpaRepository.findById(id.toString()).get().value.inputStream() }
        } else {
            null
        }
    }

    override fun save(blob: Blob): UUID {
        val id: UUID = UUID.randomUUID()

        blobJpaRepository.save(
            BlobJpaEntity(
                id = id.toString(),
            ),
        )

        blobValueJpaRepository.save(
            BlobValueJpaEntity(
                id = id.toString(),
                value = blob.byteArray,
            ),
        )

        return id
    }

}
