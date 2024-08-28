package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.Events
import by.sf.bot.repository.impl.EventInfoRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class EventController(
    private val eventInfoRepository: EventInfoRepository
) {

    @QueryMapping
    fun getAllEvents(): Flux<Events>{
        return eventInfoRepository.getAllEvents()
    }

    @MutationMapping
    fun addEvent(@Argument eventModel: Events): Mono<Events> {
        return eventInfoRepository.save(eventModel)
    }




    @MutationMapping
    fun deleteEvent(@Argument eventId: Int): Mono<Boolean> {
        return eventInfoRepository.delete(eventId)
    }

    @MutationMapping
    fun updateEvent(
        @Argument oldEventDate: String,
        @Argument event: Events
    ): Mono<Boolean> {
        return eventInfoRepository.update(oldEventDate, event)
    }


}