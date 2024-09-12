package by.sf.bot.service


import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import org.springframework.stereotype.Service
import java.io.File
import java.time.Duration

@Service
class DockerService {

    fun restartDocker(containerName: String) {
        try {
            val processBuilder = ProcessBuilder("bash", "/home/Nikolay/speedfriendingBot/restart_container.sh", containerName)

            processBuilder.directory(File("/home/Nikolay/speedfriendingBot"))
            val process = processBuilder.start()

            // Ожидание завершения выполнения процесса
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                println("Container $containerName restarted successfully.")
            } else {
                println("Failed to restart container $containerName.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
