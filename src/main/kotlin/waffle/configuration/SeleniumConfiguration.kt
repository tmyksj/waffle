package waffle.configuration

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class SeleniumConfiguration {

    @PostConstruct
    fun configure() {
        // Use Java 11+ HTTP Client
        // https://www.selenium.dev/blog/2022/using-java11-httpclient/
        System.setProperty("webdriver.http.factory", "jdk-http-client")
    }

}
