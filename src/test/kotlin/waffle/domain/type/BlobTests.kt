package waffle.domain.type

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BlobTests {

    @MethodSource
    @ParameterizedTest
    fun structuralEquality(a: Blob, b: Any?, expected: Boolean) {
        SoftAssertions.assertSoftly {
            it.assertThat(a == b).isEqualTo(expected)
            it.assertThat(a.hashCode() == b.hashCode()).isEqualTo(expected)
        }
    }

    companion object {

        @JvmStatic
        fun structuralEquality(): List<Arguments> {
            val byteArray1: ByteArray = "ByteArray1".toByteArray()
            val byteArray2: ByteArray = "ByteArray2".toByteArray()
            val any1: Any = "Any1"

            return listOf(
                Arguments.of(Blob { byteArray1 }, Blob { byteArray1 }, true),
                Arguments.of(Blob { byteArray1 }, Blob { byteArray2 }, false),
                Arguments.of(Blob { byteArray1 }, any1, false),
                Arguments.of(Blob { byteArray1 }, null, false),
            )
        }

    }

}
