package by.sf.bot.service

import by.sf.bot.component.TelegramBot
import by.sf.bot.models.FullUserMatches
import by.sf.bot.repository.blocking.RandomCoffeeBlockingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class AsyncMatchingService(
    private val randomCoffeeBlockingRepository: RandomCoffeeBlockingRepository,
    private val matchingService: MatchingService
) {

    suspend fun recalculateAllMatches() {
        withContext(Dispatchers.IO) {
            val allUsers = randomCoffeeBlockingRepository.getAllRandomCoffeeAccounts()
            allUsers.forEach { user ->
                val matches = matchingService.findMatches(user.userId!!)
                val matchedUserIds = matches.map { it.userId }

                // Сохраняем текущие значения viewedUsers
                val currentViewedUsers = TelegramBot.userMatchesMap[user.userId]?.viewedUsers ?: mutableListOf()

                // Обновляем compatibleUsers
                val fullUserMatches = FullUserMatches(
                    compatibleUsers = matchedUserIds.toMutableList(),
                    viewedUsers = currentViewedUsers
                )
                TelegramBot.userMatchesMap[user.userId!!] = fullUserMatches
            }

            matchingService.saveAllMatchesInDB()
        }
    }
}