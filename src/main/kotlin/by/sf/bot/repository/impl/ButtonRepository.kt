package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Buttons.Companion.BUTTONS
import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.repository.blocking.ButtonBlockingRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class ButtonRepository(
    private val dsl: DSLContext,
    private val buttonBlockingRepository: ButtonBlockingRepository
) {
    fun getAllButtonsByMenuTitle(menuTitle: String): List<Buttons> {
        return dsl.select(BUTTONS.asterisk())
            .from(BUTTONS)
            .where(
                BUTTONS.MENU_ID.eq(
                    dsl.select(MENU_INFO.MENU_ID)
                        .from(MENU_INFO)
                        .where(MENU_INFO.TITLE.eq(menuTitle))
                )
            )
            .map { it.into(Buttons::class.java) }
            .sortedBy { it.position }
    }

    fun save(button: Buttons): Mono<Buttons?> {
        return Mono.fromSupplier {
            button.dateCreated = LocalDate.now()
            val newButtonRecord = dsl.newRecord(BUTTONS)
            newButtonRecord.from(button)
            newButtonRecord.reset(BUTTONS.BUTTON_ID)
            newButtonRecord.store()
            return@fromSupplier newButtonRecord.into(Buttons::class.java)
        }
    }

    fun update(menuTitle: String, label: String, button: Buttons): Mono<Boolean> {
        return Mono.fromSupplier {
            val oldButton: Buttons = buttonBlockingRepository.getButtonByMenuAndLabel(menuTitle, label)
                ?: return@fromSupplier false

            return@fromSupplier dsl.update(BUTTONS)
                .set(BUTTONS.MENU_ID, button.menuId ?: oldButton.menuId)
                .set(BUTTONS.POSITION, button.position ?: oldButton.position)
                .set(BUTTONS.LABEL, button.label ?: oldButton.label)
                .set(BUTTONS.ACTION_TYPE, button.actionType ?: oldButton.actionType)
                .set(BUTTONS.ACTION_DATA, button.actionData ?: oldButton.actionData)
                .set(BUTTONS.DATE_CREATED, LocalDate.now())
                .where(BUTTONS.BUTTON_ID.eq(oldButton.buttonId))
                .execute() == 1
        }
    }

    fun delete(menuTitle: String, label: String): Mono<Boolean>{
        return Mono.fromSupplier {

            dsl.deleteFrom(BUTTONS)
                .where(BUTTONS.MENU_ID.eq(
                    dsl.select(MENU_INFO.MENU_ID).from(MENU_INFO)
                        .where(MENU_INFO.TITLE.eq(menuTitle))

                )).and(BUTTONS.LABEL.eq(label))
                .execute() ==1
        }
    }
}