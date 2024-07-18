package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.pojos.Reminders
import by.sf.bot.repository.impl.ReminderRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class ReminderController(
    private val reminderRepository: ReminderRepository
) {
    @MutationMapping
    open fun addReminder(@Argument reminder: Reminders): Mono<Reminders> {
        return reminderRepository.addReminder(reminder)
    }
}