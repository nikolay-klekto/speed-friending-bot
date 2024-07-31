package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Events.Companion.EVENTS
import by.sf.bot.jooq.tables.pojos.Events
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDate

@Repository
class EventInfoRepository(
    private val dsl: DSLContext
) {
    fun save(event: Events): Mono<Events> {
        return Mono.fromSupplier {
            event.dateCreated = LocalDate.now()
            val newEventRecord = dsl.newRecord(EVENTS)
            newEventRecord.from(event)
            newEventRecord.reset(EVENTS.EVENT_ID)
            newEventRecord.store()
            return@fromSupplier newEventRecord.into(Events::class.java)
        }
    }

    fun delete(eventId: Int): Mono<Boolean>{
        return Mono.fromCallable {
            dsl.deleteFrom(EVENTS)
                .where(EVENTS.EVENT_ID.eq(eventId))
                .execute() == 1
        }.subscribeOn(Schedulers.boundedElastic())
    }
}