package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.Events.Companion.EVENTS
import by.sf.bot.jooq.tables.pojos.Events
import by.sf.bot.jooq.tables.pojos.MenuInfo
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
open class EventBlockingRepository(
    private val dsl: DSLContext
) {
    fun getEventById(eventId: Int): Events {
        return dsl.select(EVENTS.EVENT_DATE, EVENTS.EVENT_DATE_TEXT).from(EVENTS)
            .where(EVENTS.EVENT_ID.eq(eventId))
            .first()
            .map { it.into(Events::class.java) }
    }

    fun getEventByDateText(eventDateText: String): Events {
        return dsl.select(EVENTS.asterisk()).from(EVENTS)
            .where(EVENTS.EVENT_DATE_TEXT.eq(eventDateText))
            .first()
            .map { it.into(Events::class.java) }
    }

    fun getLastEventId(): Int? {
        return dsl.select(EVENTS.EVENT_ID).from(EVENTS)
            .max()
            .map { it.into(Int::class.java) }
    }
}