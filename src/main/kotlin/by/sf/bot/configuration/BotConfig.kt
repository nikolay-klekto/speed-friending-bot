package by.sf.bot.configuration

import by.sf.bot.component.TelegramBot
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import by.sf.bot.repository.impl.ButtonRepository
import by.sf.bot.repository.impl.MainBotInfoRepository
import by.sf.bot.repository.impl.MenuInfoRepository
import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
class BotConfig {

    @Bean
    fun telegramBotsApi(telegramBot: TelegramBot): TelegramBotsApi {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        try {
            botsApi.registerBot(telegramBot)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
        return botsApi
    }

    @Bean
    fun telegramBot(
        mainBotInfoRepository: MainBotInfoRepository,
        menuInfoBlockingRepository: MenuInfoBlockingRepository,
        buttonRepository: ButtonRepository,
        menuInfoRepository: MenuInfoRepository,
        dsl: DSLContext
    ): TelegramBot {
        return TelegramBot(
            mainBotInfoRepository,
            menuInfoBlockingRepository,
            buttonRepository,
            menuInfoRepository,
            dsl
        )
    }
}