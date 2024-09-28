package by.sf.bot.controller

import by.sf.bot.component.AppManager
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestartManagerController(
    private val appManager: AppManager
) {

    @MutationMapping
    fun restartApp(): Boolean {
        appManager.restart()
        return true
    }

    @MutationMapping
    fun restartAppAt3AM(): Boolean {
        appManager.restartAt3AM()
        return true
    }

}