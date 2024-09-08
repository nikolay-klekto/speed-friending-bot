package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.pojos.RemindDates
import by.sf.bot.repository.impl.RemindDatesRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class RemindDateController(
    private val remindDatesRepository: RemindDatesRepository
) {

    @QueryMapping
    fun getAllReminders(): Flux<RemindDates> {
        return remindDatesRepository.getAllReminders()
    }

    @MutationMapping
    open fun addRemindDate(@Argument remindDate: RemindDates): Mono<RemindDates> {
        return remindDatesRepository.addRemindDate(remindDate)
    }
}