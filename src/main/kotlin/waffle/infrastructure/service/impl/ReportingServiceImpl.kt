package waffle.infrastructure.service.impl

import org.springframework.stereotype.Service
import waffle.core.component.ContentTypeComponent
import waffle.domain.service.ReportingService
import java.io.Closeable
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

@Service
class ReportingServiceImpl(
    private val contentTypeComponent: ContentTypeComponent,
) : ReportingService {

    @OptIn(ExperimentalPathApi::class)
    override fun compareImages(a: List<ByteArray>, b: List<ByteArray>): ByteArray {
        return createTempDirectory().run {
            Closeable { toFile().deleteRecursively() }.use { _ ->
                path("regconfig.json").writeText(
                    """
                        {
                            "core": {
                                "actualDir": "actual",
                                "workingDir": ".reg",
                                "ximgdiff": {
                                    "invocationType": "none"
                                }
                            }
                        }
                    """.trimIndent(),
                )

                path(".reg", "expected").createDirectories()

                a.forEachIndexed { index, it ->
                    path(".reg", "expected", "${index + 1}${contentTypeComponent.guessExtension(it)}").writeBytes(it)
                }

                path("actual").createDirectories()

                b.forEachIndexed { index, it ->
                    path("actual", "${index + 1}${contentTypeComponent.guessExtension(it)}").writeBytes(it)
                }

                ProcessBuilder("reg-suit", "--config", "regconfig.json", "compare")
                    .directory(toFile())
                    .start()
                    .waitFor()

                ZipOutputStream(FileOutputStream(path(".reg.zip").toFile())).use { zipOutputStream ->
                    val startIndex: Int = path(".reg").toString().length + 1

                    path(".reg").walk().forEach {
                        zipOutputStream.putNextEntry(ZipEntry(it.toString().substring(startIndex)))
                        zipOutputStream.write(it.readBytes())
                        zipOutputStream.closeEntry()
                    }
                }

                path(".reg.zip").readBytes()
            }
        }
    }

    private fun Path.path(vararg args: String): Path {
        return resolve(fileSystem.getPath(args.first(), *(args.drop(1).toTypedArray())))
    }

}
