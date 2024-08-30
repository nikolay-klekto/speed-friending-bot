package by.sf.bot.configuration

import by.sf.bot.component.TelegramBot
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import by.sf.bot.repository.blocking.UserBlockingRepository
import by.sf.bot.repository.impl.*
import by.sf.bot.service.AsyncMatchingService
import by.sf.bot.service.MatchingService
import by.sf.bot.service.NotificationService
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
        userBlockingRepository: UserBlockingRepository,
        randomCoffeeRepository: RandomCoffeeRepository,
        randomCoffeeVariantsRepository: RandomCoffeeVariantsRepository,
        matchRepository: MatchRepository,
        matchingService: MatchingService,
        asyncMatchingService: AsyncMatchingService
    ): TelegramBot {
        return TelegramBot(
            mainBotInfoRepository,
            menuInfoBlockingRepository,
            buttonRepository,
            userBlockingRepository,
            randomCoffeeRepository,
            randomCoffeeVariantsRepository,
            matchRepository,
            matchingService,
            asyncMatchingService
        )
    }
}