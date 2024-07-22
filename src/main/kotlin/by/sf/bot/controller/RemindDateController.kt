package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.RemindDates
import by.sf.bot.repository.impl.RemindDatesRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class RemindDateController(
    private val remindDatesRepository: RemindDatesRepository
) {
    @MutationMapping
    open fun addRemindDate(@Argument remindDate: RemindDates): Mono<RemindDates> {
        return remindDatesRepository.addRemindDate(remindDate)
    }
}