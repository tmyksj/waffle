package waffle.configuration.bean

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggerBean {

    @Bean
    @Scope(value = "prototype")
    fun logger(injectionPoint: InjectionPoint): Logger {
        return LoggerFactory.getLogger(injectionPoint.member.declaringClass)
    }

}
