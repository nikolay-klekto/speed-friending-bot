package by.sf.bot.service

import by.sf.bot.component.TelegramBot
import by.sf.bot.jooq.tables.UserMatches.Companion.USER_MATCHES
import by.sf.bot.repository.blocking.UserBlockingRepository
import by.sf.bot.repository.impl.EventInfoRepository
import by.sf.bot.repository.impl.RemindDatesRepository
import org.jooq.DSLContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
class NotificationService(
    private val eventRepository: EventInfoRepository,
    private val remindDateRepository: RemindDatesRepository,
    private val userRepository: UserBlockingRepository,
    private val telegramBot: TelegramBot,
    private val dsl: DSLContext
) {

    @Scheduled(cron = "0 0 8 * * *")  // Срабатывает каждый день в 8 утра
    fun sendReminders() {

        val todayRemindDatesList = remindDateRepository.getAllRemindDatesForToday()

        val eventsIdForRemindList = todayRemindDatesList.distinctBy { it.eventId }
            .map { it.eventId }

        val usersForRemind = userRepository.getAllUsersChatIdByRemindStatus(eventsIdForRemindList)

        usersForRemind.forEach { currentUser ->
            if(currentUser.reminders == REMIND_STATUS_FOR_ALL_EVENTS){
                todayRemindDatesList.forEach {
                    telegramBot.sendMessage(currentUser.telegramId!!, it.description!!)
                }
            }
            todayRemindDatesList.forEach{
                if(currentUser.reminders == it.eventId.toString()){
                    telegramBot.sendMessage(currentUser.telegramId!!, it.description!!)
                }
            }
        }
    }

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // каждые 6 часов
    fun syncUserMatchesWithDatabase() {
        TelegramBot.userMatchesMap.forEach { (userId, matches) ->
            val compatibleUsersString = matches.compatibleUsers.joinToString(",")
            val viewedUsersString = matches.viewedUsers.joinToString(",")

            dsl.insertInto(USER_MATCHES)
                .set(USER_MATCHES.USER_ID, userId)
                .set(USER_MATCHES.COMPATIBLE_USERS, compatibleUsersString)
                .set(USER_MATCHES.VIEWED_USERS, viewedUsersString)
                .onDuplicateKeyUpdate()
                .set(USER_MATCHES.COMPATIBLE_USERS, compatibleUsersString)
                .set(USER_MATCHES.VIEWED_USERS, viewedUsersString)
                .set(USER_MATCHES.DATE_CREATED, LocalDate.now())
                .execute()
        }
    }

    companion object{
        private const val REMIND_STATUS_FOR_ALL_EVENTS = "all"
    }
}


