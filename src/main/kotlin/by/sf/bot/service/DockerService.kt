import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientBuilder
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import com.github.dockerjava.transport.DockerHttpClient
import java.time.Duration

fun createDockerClient(): DockerClient {
    // Настройка для подключения к Unix-сокету
    val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock")
        .build()

    val httpClient: DockerHttpClient = ApacheDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .maxConnections(100)
        .connectionTimeout(Duration.ofSeconds(30))
        .responseTimeout(Duration.ofSeconds(45))
        .build()

    return DockerClientBuilder.getInstance(config)
        .withDockerHttpClient(httpClient)
        .build()
}

class DockerService {

    fun restartDocker(containerName: String) {
        val dockerClient = createDockerClient()

        // Поиск контейнера по имени
        val containers: List<Container> = dockerClient.listContainersCmd().exec()
        val container = containers.find { it.names.any { name -> name == "/$containerName" } }

        if (container != null) {
            val containerId = container.id
            println("Container found: $containerId. Restarting...")

            // Перезапуск контейнера
            dockerClient.restartContainerCmd(containerId).exec()
            println("Container $containerId restarted successfully.")
        } else {
            println("Container with name $containerName not found.")
        }
    }
}
