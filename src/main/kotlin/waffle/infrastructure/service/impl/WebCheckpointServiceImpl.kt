package waffle.infrastructure.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionTemplate
import waffle.core.component.BrowserComponent
import waffle.core.component.ContentTypeComponent
import waffle.core.type.Blob
import waffle.domain.entity.WebCheckpoint
import waffle.domain.model.WebSnapshot
import waffle.domain.repository.WebCheckpointRepository
import waffle.domain.service.WebCheckpointService
import java.io.Closeable
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

@Service
class WebCheckpointServiceImpl(
    private val platformTransactionManager: PlatformTransactionManager,
    private val browserComponent: BrowserComponent,
    private val contentTypeComponent: ContentTypeComponent,
    private val webCheckpointRepository: WebCheckpointRepository,
) : WebCheckpointService {

    @OptIn(ExperimentalPathApi::class)
    override fun create(entity: WebCheckpoint) {
        val transactionTemplate: TransactionTemplate = TransactionTemplate().apply {
            propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            transactionManager = platformTransactionManager
        }

        try {
            val e1: WebCheckpoint = transactionTemplate.execute {
                webCheckpointRepository.save(entity.start())
            }.let { checkNotNull(it) }

            val snapshots: List<WebSnapshot> = e1.flow.compositions.map {
                Thread.sleep(it.delayMs)

                val screenshot: ByteArray = browserComponent.captureScreenshot(
                    url = it.resource,
                    width = it.widthPx.toInt(),
                )

                WebSnapshot(
                    resource = it.resource,
                    widthPx = it.widthPx,
                    screenshot = Blob { screenshot.inputStream() },
                )
            }

            val output: ByteArray = createTempDirectory().run {
                Closeable { toFile().deleteRecursively() }.use { _ ->
                    path("output").createDirectories()

                    snapshots.forEachIndexed { index, it ->
                        path("output", "${index + 1}${contentTypeComponent.guessExtension(it.screenshot.byteArray)}")
                            .writeBytes(it.screenshot.byteArray)
                    }

                    ZipOutputStream(FileOutputStream(path("output.zip").toFile())).use { zipOutputStream ->
                        val startIndex: Int = path("output").toString().length + 1

                        path("output").walk().forEach {
                            zipOutputStream.putNextEntry(ZipEntry(it.toString().substring(startIndex)))
                            zipOutputStream.write(it.readBytes())
                            zipOutputStream.closeEntry()
                        }
                    }

                    path("output.zip").readBytes()
                }
            }

            transactionTemplate.execute {
                webCheckpointRepository.save(e1.complete(Blob { output.inputStream() }, snapshots))
            }
        } catch (e: Exception) {
            transactionTemplate.execute {
                webCheckpointRepository.save(entity.fail())
            }
        }
    }

    private fun Path.path(vararg args: String): Path {
        return resolve(fileSystem.getPath(args.first(), *(args.drop(1).toTypedArray())))
    }

}
