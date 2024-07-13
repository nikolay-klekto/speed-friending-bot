package by.sf.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
//@EnableCaching
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}