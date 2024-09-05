package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Events.Companion.EVENTS
import by.sf.bot.jooq.tables.RemindDates.Companion.REMIND_DATES
import by.sf.bot.jooq.tables.pojos.Events
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.repository.blocking.ButtonBlockingRepository
import by.sf.bot.repository.blocking.EventBlockingRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDate

@Repository
class EventInfoRepository(
    private val dsl: DSLContext,
    private val eventBlockingRepository: EventBlockingRepository,
    private val buttonBlockingRepository: ButtonBlockingRepository,
    private val remindDatesRepository: RemindDatesRepository
) {

    fun getAllEvents(): Flux<Events> {
        return Flux.fromIterable(
            dsl.select(EVENTS.asterisk()).from(EVENTS)
        ).map { it.into(Events::class.java) }
    }

    fun save(event: Events): Mono<Events> {
        return Mono.fromSupplier {
            event.dateCreated = LocalDate.now()
            val newEventId = if(eventBlockingRepository.getLastEventId() == null){1}
            else{
                eventBlockingRepository.getLastEventId()?.plus(1)
            }

            event.eventId = newEventId
            val newEventRecord = dsl.newRecord(EVENTS)
            newEventRecord.from(event)
            val saveResult = newEventRecord.store() == 1
            val result = newEventRecord.into(Events::class.java)
            if(saveResult){
                buttonBlockingRepository.saveNewButtonFromEvent(result)
            }
            return@fromSupplier result
        }
    }

    fun delete(eventId: Int): Mono<Boolean>{
        return Mono.fromCallable {

            val eventDateText = dsl.select(EVENTS.EVENT_DATE_TEXT)
                .from(EVENTS)
                .where(EVENTS.EVENT_ID.eq(eventId))
                .first()
                .map { it.into(String::class.java) }

            val result2 = remindDatesRepository.deleteRemindDateByEventId(eventId)

            val result = dsl.deleteFrom(EVENTS)
                .where(EVENTS.EVENT_ID.eq(eventId))
                .execute() == 1

            var result3 = false

            if(result){
                result3 = buttonBlockingRepository.deleteFromDeletingEvent(eventDateText)
            }
            return@fromCallable result && result2 && result3
        }.subscribeOn(Schedulers.boundedElastic())
    }

    fun update(oldEventDate: String, eventInfo: Events): Mono<Boolean> {
        return Mono.fromSupplier {
            val oldEventModel: Events = eventBlockingRepository.getEventByDateText(oldEventDate)

            if(eventInfo.eventDateText != null || eventInfo.googleFormUrl != null){
                buttonBlockingRepository.updateFromUpdatingEvent(oldEventModel, eventInfo)
            }

            val result = dsl.update(EVENTS)
                .set(EVENTS.EVENT_DATE, eventInfo.eventDate ?: oldEventModel.eventDate)
                .set(EVENTS.EVENT_DATE_TEXT, eventInfo.eventDateText ?: oldEventModel.eventDateText)
                .set(EVENTS.GOOGLE_FORM_URL, eventInfo.googleFormUrl ?: oldEventModel.googleFormUrl)
                .set(EVENTS.DATE_CREATED, LocalDate.now())
                .where(EVENTS.EVENT_ID.eq(oldEventModel.eventId))
                .execute() == 1

            return@fromSupplier result
        }
    }
}