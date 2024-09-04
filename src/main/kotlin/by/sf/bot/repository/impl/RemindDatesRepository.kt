package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.RemindDates.Companion.REMIND_DATES
import by.sf.bot.jooq.tables.Users.Companion.USERS
import by.sf.bot.jooq.tables.pojos.RemindDates
import by.sf.bot.repository.blocking.UserBlockingRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class RemindDatesRepository(
    private val dsl: DSLContext,
    private val userBlockingRepository: UserBlockingRepository
) {

    fun getAllRemindDatesForToday(): List<RemindDates> {
        return dsl.select(REMIND_DATES.asterisk()).from(REMIND_DATES)
            .where(REMIND_DATES.REMIND_DATE.eq(LocalDate.now()))
            .map { it.into(RemindDates::class.java) }
    }

    fun addRemindDate(remindDate: RemindDates): Mono<RemindDates> {
        return Mono.fromSupplier {
            remindDate.dateCreated = LocalDate.now()
            val newRemindDateRecord = dsl.newRecord(REMIND_DATES)
            newRemindDateRecord.from(remindDate)
            newRemindDateRecord.reset(REMIND_DATES.ID)
            newRemindDateRecord.store()
            return@fromSupplier newRemindDateRecord.into(RemindDates::class.java)

        }
    }

    fun deleteRemindDateByEventId(eventId: Int): Boolean{

        val currentRemindersList = userBlockingRepository.getAllUsersByEventIdListNotIncludedStatusAll(listOf(eventId))

        currentRemindersList.forEach {currentUserModel ->
            if (!currentUserModel.reminders.isNullOrEmpty()) {
                // Преобразуем строку в список Int
                val reminderIds = currentUserModel.reminders!!.split(",").map { it.trim().toInt() }.toMutableList()

                // Удаляем нужный reminderId
                reminderIds.remove(eventId)

                // Преобразуем список обратно в строку
                val newReminders = reminderIds.joinToString(",")

                dsl.update(USERS)
                    .set(USERS.REMINDERS, newReminders)
                    .where(USERS.USER_ID.eq(currentUserModel.userId))
                    .execute()
            }

        }
        return dsl.deleteFrom(REMIND_DATES)
            .where(REMIND_DATES.EVENT_ID.eq(eventId))
            .execute() == 1
    }
}