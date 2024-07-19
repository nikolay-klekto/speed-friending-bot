package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.jooq.tables.pojos.EventInfo
import by.sf.bot.jooq.tables.references.EVENT_INFO
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDate

@Repository
class EventInfoRepository(
    private val dsl: DSLContext
) {

    fun save(event: EventInfo): Mono<EventInfo> {
        return Mono.fromSupplier {
            event.dateCreated = LocalDate.now()
            val newEventRecord = dsl.newRecord(EVENT_INFO)
            newEventRecord.from(event)
            newEventRecord.reset(EVENT_INFO.EVENT_ID)
            newEventRecord.store()
            return@fromSupplier newEventRecord.into(EventInfo::class.java)
        }
    }

    fun delete(eventId: Int): Mono<Boolean>{
        return Mono.fromCallable {
            dsl.deleteFrom(EVENT_INFO)
                .where(EVENT_INFO.EVENT_ID.eq(eventId))
                .execute() == 1
        }.subscribeOn(Schedulers.boundedElastic())
    }
}