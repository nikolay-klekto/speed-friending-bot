package by.sf.bot.service

import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
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
        checkDocker()
        printPath()
        val processBuilder = ProcessBuilder("bash", "-c", "sudo docker restart $containerName")
        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Container restarted successfully")
        } else {
            throw IOException("Failed to restart the container")
        }
    }

    fun checkDocker(): Boolean {
        val processBuilder = ProcessBuilder("bash", "-c", "export PATH=\$PATH:/usr/bin && docker ps")
        val process = processBuilder.start()

        // Чтение стандартного вывода
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()
        reader.lines().forEach { line -> output.append(line).append("\n") }

        // Чтение ошибок (если есть)
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))
        val errors = StringBuilder()
        errorReader.lines().forEach { line -> errors.append(line).append("\n") }

        // Ожидание завершения процесса
        val exitCode = process.waitFor()

        // Выводим логи и ошибки
        if (exitCode == 0) {
            println("Command succeeded. Output:\n$output")
        } else {
            println("Command failed with exit code $exitCode. Errors:\n$errors")
        }

        return exitCode == 0
    }

    fun printPath() {
        val processBuilder = ProcessBuilder("bash", "-c", "echo \$PATH")
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val path = reader.readText()
        println("Current PATH: $path")
    }
}