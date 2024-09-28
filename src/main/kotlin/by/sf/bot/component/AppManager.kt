package by.sf.bot.component

import by.sf.bot.BotApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@Component
class AppManager {
    @Autowired
    private val context: ApplicationContext? = null

    private val scheduler = Executors.newScheduledThreadPool(1)

    fun restartAt3AM() {
        val delay = calculateDelayUntil3AM()

        scheduler.schedule({
            restart()
        }, delay, TimeUnit.MILLISECONDS)
    }

    private fun calculateDelayUntil3AM(): Long {
        val now = LocalDateTime.now()
        var next3AM = now.toLocalDate().atTime(3, 0)

        if (now.isAfter(next3AM)) {
            next3AM = next3AM.plusDays(1)
        }

        val duration = Duration.between(now, next3AM)
        return duration.toMillis()
    }

    fun restart() {
        val thread = Thread {
            try {
                Thread.sleep(1000)
                SpringApplication.exit(context, ExitCodeGenerator { 0 })
                SpringApplication.run(BotApplication::class.java)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        thread.isDaemon = false
        thread.start()
    }
}