package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.EventInfo
import by.sf.bot.jooq.tables.pojos.MainBotInfo
import by.sf.bot.repository.impl.EventInfoRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class EventController(
    private val eventInfoRepository: EventInfoRepository
) {

    @MutationMapping
    open fun addEvent(@Argument eventModel: EventInfo): Mono<EventInfo> {
        return eventInfoRepository.save(eventModel)
    }

    @MutationMapping
    open fun deleteEvent(@Argument eventId: Int): Mono<Boolean>{
        return eventInfoRepository.delete(eventId)
    }
}