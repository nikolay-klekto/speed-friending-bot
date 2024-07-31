package by.sf.bot.service

import by.sf.bot.component.TelegramBot
import by.sf.bot.models.FullUserMatches
import by.sf.bot.repository.blocking.RandomCoffeeBlockingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class AsyncMatchingService(
    private val randomCoffeeBlockingRepository: RandomCoffeeBlockingRepository,
    private val matchingService: MatchingService
) {

    suspend fun recalculateAllMatches() {
        withContext(Dispatchers.IO) {
            val allUsersId = randomCoffeeBlockingRepository.getAllRandomCoffeeAccountsUsersId()
            val deferredResults = allUsersId.map { userId ->
                async {
                    val matches = matchingService.findMatches(userId)
                    val matchedUserIds = matches.map { it.userId }

                    // Сохраняем текущие значения viewedUsers
                    val currentViewedUsers = TelegramBot.userMatchesMap[userId]?.viewedUsers ?: mutableListOf()

                    // Обновляем compatibleUsers
                    val fullUserMatches = FullUserMatches(
                        compatibleUsers = matchedUserIds.toMutableList(),
                        viewedUsers = currentViewedUsers
                    )
                    TelegramBot.userMatchesMap[userId] = fullUserMatches
                }
            }
            deferredResults.awaitAll()
            matchingService.saveAllMatchesInDB()
        }
    }
}