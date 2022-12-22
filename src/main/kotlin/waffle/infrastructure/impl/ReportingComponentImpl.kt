package waffle.infrastructure.impl

import org.springframework.stereotype.Component
import waffle.component.ContentTypeComponent
import waffle.component.ReportingComponent
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

@Component
class ReportingComponentImpl(
    private val contentTypeComponent: ContentTypeComponent,
) : ReportingComponent {

    @OptIn(ExperimentalPathApi::class)
    override fun compareImages(a: List<ByteArray>, b: List<ByteArray>): ByteArray {
        val extMap: Map<String, String> = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
        )

        return createTempDirectory().run {
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
                path(".reg", "expected", "${index + 1}.${extMap[contentTypeComponent.guess(it)] ?: ""}").writeBytes(it)
            }

            path("actual").createDirectories()

            b.forEachIndexed { index, it ->
                path("actual", "${index + 1}.${extMap[contentTypeComponent.guess(it)] ?: ""}").writeBytes(it)
            }

            ProcessBuilder("reg-suit", "--config", "regconfig.json", "compare")
                .directory(toFile())
                .start()
                .waitFor()

            ZipOutputStream(FileOutputStream(path(".reg.zip").toFile())).use { zipOutputStream ->
                val startIndex: Int = path(".reg").toString().length

                path(".reg").walk().forEach {
                    zipOutputStream.putNextEntry(ZipEntry(it.toString().substring(startIndex)))
                    zipOutputStream.write(it.readBytes())
                    zipOutputStream.closeEntry()
                }
            }

            path(".reg.zip").readBytes().also { toFile().deleteRecursively() }
        }
    }

    private fun Path.path(vararg args: String): Path {
        return resolve(fileSystem.getPath(args.first(), *(args.drop(1).toTypedArray())))
    }

}