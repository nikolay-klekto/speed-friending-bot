//package by.sf.bot.repository.impl
//
//import by.sf.bot.jooq.tables.Reminders.Companion.REMINDERS
//import by.sf.bot.jooq.tables.pojos.Reminders
//import org.jooq.DSLContext
//import org.springframework.stereotype.Repository
//import reactor.core.publisher.Mono
//import java.time.LocalDate
//
//@Repository
//class ReminderRepository(
//    private val dsl: DSLContext
//) {
//    fun addReminder(reminder: Reminders): Mono<Reminders> {
//        return Mono.fromSupplier {
//            reminder.dateCreated = LocalDate.now()
//            val newReminderRecord = dsl.newRecord(REMINDERS)
//            newReminderRecord.from(reminder)
//            newReminderRecord.reset(REMINDERS.REMINDER_ID)
//            newReminderRecord.store()
//            return@fromSupplier newReminderRecord.into(Reminders::class.java)
//
//        }
//    }
//}