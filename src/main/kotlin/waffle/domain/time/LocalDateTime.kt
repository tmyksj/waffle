package waffle.domain.time

import java.time.LocalDateTime

/**
 * Obtains the current date-time from the system clock in the default time-zone.
 *
 * @return the current date-time using the system clock and default time-zone.
 */
fun now(): LocalDateTime {
    return LocalDateTime.now().let {
        it.minusNanos(it.nano % 1000L)
    }
}
