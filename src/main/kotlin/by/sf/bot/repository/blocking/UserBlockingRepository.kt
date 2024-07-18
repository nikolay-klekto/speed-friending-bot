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

    fun update(chatId: Long, remindStatus: Boolean): Boolean {
         return dsl.update(USERS)
                .set(USERS.REMIND_STATUS, remindStatus)
                .set(USERS.DATE_CREATED, LocalDate.now())
                .where(USERS.TELEGRAM_ID.eq(chatId))
                .execute() == 1
        }
    }

