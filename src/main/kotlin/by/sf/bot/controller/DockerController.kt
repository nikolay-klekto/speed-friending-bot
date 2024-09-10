package by.sf.bot.controller

import by.sf.bot.service.DockerService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DockerController(
    private val dockerService: DockerService
) {
    @MutationMapping
    fun restartDocker(@Argument containerName: String): String {
        return try {
            dockerService.restartDockerContainer(containerName)
            "Container restarted successfully"
        } catch (e: Exception) {
            "Failed to restart container: ${e.message}"
        }
    }

    @MutationMapping
    fun scheduleDockerRestart(@Argument containerName: String): String {
        dockerService.scheduleRestart(containerName)
        return "Container will be restarted at 2 AM tomorrow"
    }
}