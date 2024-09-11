package by.sf.bot.service

import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.newsclub.net.unix.AFUNIXSocketAddress
import org.newsclub.net.unix.AFUNIXSocket
import java.io.File
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory

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

    fun restartContainerByName(containerName: String) {
        val processBuilder = ProcessBuilder("bash", "-c", "sudo /usr/local/bin/docker restart $containerName")
        val process = processBuilder.start()
        val exitCode = process.waitFor()

        if (exitCode == 0) {
            println("Container restarted successfully")
        } else {
            throw IOException("Failed to restart the container")
        }
    }

    fun restartDockerContainer(containerName: String): Boolean {
        val containerId = getContainerIdByName(containerName)

        if (containerId != null) {
            return restartContainer(containerId)
        } else {
            println("Container with name $containerName not found")
            return false
        }
    }

    fun getContainerIdByName(containerName: String): String? {
        val client = OkHttpClient.Builder()
            .socketFactory(UnixDomainSocketFactory(File("/var/run/docker.sock")))  // Используем наш адаптер Unix-сокетов
            .build()

        val request = Request.Builder()
            .url("http://localhost/containers/json")  // Не изменяйте этот URL
            .get()
            .build()

        val response: Response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: return null
            val containers = JSONArray(responseBody)

            for (i in 0 until containers.length()) {
                val container = containers.getJSONObject(i)
                val names = container.getJSONArray("Names")
                for (j in 0 until names.length()) {
                    if (names.getString(j) == "/$containerName") {
                        return container.getString("Id")
                    }
                }
            }
        } else {
            println("Failed to fetch containers. Response code: ${response.code}")
        }

        return null
    }

    fun restartContainer(containerId: String): Boolean {
        val client = OkHttpClient.Builder()
            .socketFactory(UnixDomainSocketFactory(File("/var/run/docker.sock")))  // Используем наш адаптер Unix-сокетов
            .build()

        val request = Request.Builder()
            .url("http://localhost/containers/$containerId/restart")  // Не изменяйте этот URL
            .post(okhttp3.RequestBody.create(null, ByteArray(0)))  // Пустое тело POST-запроса
            .build()

        val response: Response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            println("Container $containerId restarted successfully")
            true
        } else {
            println("Failed to restart container $containerId. Response code: ${response.code}")
            false
        }
    }

    // Для работы с Unix-сокетом
    class UnixDomainSocketFactory(private val socketFile: File) : SocketFactory() {

        override fun createSocket(): Socket {
            // Создание и возврат экземпляра AFUNIXSocket, подключенного к Unix-сокету
            val socket = AFUNIXSocket.newInstance()
            socket.connect(AFUNIXSocketAddress(socketFile))
            return socket
        }

        override fun createSocket(host: String?, port: Int): Socket {
            throw UnsupportedOperationException("Unix domain sockets do not support host/port connections")
        }

        override fun createSocket(host: String?, port: Int, localHost: java.net.InetAddress?, localPort: Int): Socket {
            throw UnsupportedOperationException("Unix domain sockets do not support host/port connections")
        }

        override fun createSocket(host: InetAddress?, port: Int): Socket {
            TODO("Not yet implemented")
        }

        override fun createSocket(
            address: InetAddress?,
            port: Int,
            localAddress: InetAddress?,
            localPort: Int
        ): Socket {
            TODO("Not yet implemented")
        }
    }

}