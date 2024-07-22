package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.RemindDates.Companion.REMIND_DATES
import by.sf.bot.jooq.tables.pojos.RemindDates
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class RemindDatesRepository(
    private val dsl: DSLContext
) {

    fun getAllRemindDatesForToday(): List<RemindDates>{
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
}