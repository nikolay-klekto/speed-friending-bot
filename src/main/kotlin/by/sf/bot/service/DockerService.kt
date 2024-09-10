package by.sf.bot.service

import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class DockerService(
    private val taskScheduler: TaskScheduler
) {

    fun scheduleRestart(containerName: String) {
        // Определяем дату и время 2 часов ночи следующего дня
        val now = LocalDateTime.now()
        val nextRun = now.plusDays(1).withHour(2).withMinute(0).withSecond(0).withNano(0)

        // Рассчитываем задержку до этого времени
        val delay = ChronoUnit.MILLIS.between(now, nextRun)

        // Планируем задачу
        taskScheduler.schedule({
            restartDockerContainer(containerName)
        }, Date(System.currentTimeMillis() + delay))
    }

    fun restartDockerContainer(containerName: String) {
        val processBuilder = ProcessBuilder("bash", "-c", "docker restart $containerName")
        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Container restarted successfully")
        } else {
            throw IOException("Failed to restart the container")
        }
    }
}