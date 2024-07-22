package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.Users.Companion.USERS
import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.jooq.tables.pojos.Users
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class UserBlockingRepository(
    private val dsl: DSLContext
) {

    fun save(user: Users): Boolean {
        val newUserRecord = dsl.newRecord(USERS)
        newUserRecord.from(user)
        newUserRecord.reset(USERS.USER_ID)
        return newUserRecord.store() == 1
    }

    fun isUserExist(chatId: Long): Boolean{
        return dsl.selectCount()
            .from(USERS).where(USERS.TELEGRAM_ID.eq(chatId))
            .first()
            .map { it.into(Long::class.java) } > 0
        
    }

    fun getAllUsersChatIdByRemindStatus(eventsId: List<Int?>): List<Users>{
        val resultList = dsl.select(USERS.asterisk()).from(USERS)
            .where(USERS.REMINDERS.eq(REMIND_STATUS_FOR_ALL_EVENTS))
            .map { it.into(Users::class.java) }

        eventsId.forEach { currentEventId ->
            resultList.plus(
                dsl.select(USERS.asterisk()).from(USERS)
                    .where(USERS.REMINDERS.eq(currentEventId.toString()))
                    .map { it.into(Users::class.java) }
            )
        }
        return resultList
    }

    fun update(chatId: Long, reminders: String?): Boolean {
         return dsl.update(USERS)
                .set(USERS.REMINDERS, reminders)
                .set(USERS.DATE_CREATED, LocalDate.now())
                .where(USERS.TELEGRAM_ID.eq(chatId))
                .execute() == 1
        }

    companion object{
        private const val REMIND_STATUS_FOR_ALL_EVENTS = "all"
    }

    }

