package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.Buttons.Companion.BUTTONS
import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.Buttons
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
open class ButtonBlockingRepository(
    private val dsl: DSLContext
) {
    fun getButtonByMenuAndLabel(menuId: Int, label: String): Buttons? {
    return dsl.select(BUTTONS.asterisk()).from(BUTTONS)
            .where(BUTTONS.MENU_ID.eq(
                dsl.select(MENU_INFO.MENU_ID).from(MENU_INFO).where(MENU_INFO.MENU_ID.eq(menuId))
            ).and(BUTTONS.LABEL.eq(label)))
            .firstOrNull()
            ?.map { it.into(Buttons::class.java) }

}
}