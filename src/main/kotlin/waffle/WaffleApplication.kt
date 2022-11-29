package waffle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WaffleApplication

fun main(args: Array<String>) {
    runApplication<WaffleApplication>(*args)
}
