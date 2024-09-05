package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.Buttons.Companion.BUTTONS
import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.jooq.tables.pojos.Events
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ButtonBlockingRepository(
    private val dsl: DSLContext
) {
    fun getButtonByMenuAndLabel(menuId: Int, label: String): Buttons? {
        return dsl.select(BUTTONS.asterisk()).from(BUTTONS)
            .where(
                BUTTONS.MENU_ID.eq(
                    dsl.select(MENU_INFO.MENU_ID).from(MENU_INFO).where(MENU_INFO.MENU_ID.eq(menuId))
                ).and(BUTTONS.LABEL.eq(label))
            )
            .firstOrNull()
            ?.map { it.into(Buttons::class.java) }

    }

    fun save(button: Buttons): Buttons? {
        button.dateCreated = LocalDate.now()
        val newButtonRecord = dsl.newRecord(BUTTONS)
        newButtonRecord.from(button)
        newButtonRecord.reset(BUTTONS.BUTTON_ID)
        newButtonRecord.store()
        return newButtonRecord.into(Buttons::class.java)
    }

    fun saveNewButtonFromEvent(event: Events) {

        val buttonForWouldLikeToCheckIn = Buttons(
            menuId = 3,
            label = "анкета встречи ${event.eventDateText}",
            actionType = "url",
            actionData = event.googleFormUrl,
            dateCreated = LocalDate.now(),
            position = getNextPositionByMenuId(3)
        )

        save(buttonForWouldLikeToCheckIn)

        val buttonForLookCalendar = Buttons(
            menuId = 9,
            label = "анкета встречи ${event.eventDateText}",
            actionType = "url",
            actionData = event.googleFormUrl,
            dateCreated = LocalDate.now(),
            position = getNextPositionByMenuId(9)
        )

        save(buttonForLookCalendar)

        val buttonForSetReminder = Buttons(
            menuId = 11,
            label = "Напомнить о встрече ${event.eventDateText}",
            actionType = "callback",
            actionData = "reminder_yes_event_id:${event.eventId}",
            dateCreated = LocalDate.now(),
            position = getNextPositionByMenuId(11)
        )

        save(buttonForSetReminder)
    }

    fun deleteFromDeletingEvent(eventDateText: String): Boolean {
        return dsl.deleteFrom(BUTTONS).where(BUTTONS.LABEL.like("%$eventDateText%")).execute() == 1
    }

    fun updateFromUpdatingEvent(oldEvent: Events, newEvent: Events) {

        if (newEvent.eventDateText != null) {
            dsl.update(BUTTONS)
                .set(BUTTONS.LABEL, "анкета встречи ${newEvent.eventDateText}")
                .set(BUTTONS.DATE_CREATED, LocalDate.now())
                .where(BUTTONS.LABEL.like("анкета встречи ${oldEvent.eventDateText}"))
                .execute()

            dsl.update(BUTTONS)
                .set(BUTTONS.LABEL, "Напомнить о встрече ${newEvent.eventDateText}")
                .set(BUTTONS.DATE_CREATED, LocalDate.now())
                .where(BUTTONS.LABEL.like("Напомнить о встрече ${oldEvent.eventDateText}"))
                .execute()

        }
        if (newEvent.googleFormUrl != null) {
            dsl.update(BUTTONS)
                .set(BUTTONS.ACTION_DATA, newEvent.googleFormUrl)
                .set(BUTTONS.DATE_CREATED, LocalDate.now())
                .where(BUTTONS.ACTION_DATA.eq(oldEvent.googleFormUrl))
                .execute()
        }
    }

    fun getNextPositionByMenuId(menuId: Int): Int? {
        val t1 = BUTTONS.`as`("t1")
        val t2 = BUTTONS.`as`("t2")

        return dsl.select(coalesce(min(t1.POSITION.add(1)), 1))
            .from(t1)
            .leftJoin(t2)
            .on(t1.POSITION.add(1).eq(t2.POSITION).and(t1.MENU_ID.eq(t2.MENU_ID)))
            .where(t1.MENU_ID.eq(menuId).and(t2.POSITION.isNull()))
            .fetchOneInto(Int::class.java)
    }
}