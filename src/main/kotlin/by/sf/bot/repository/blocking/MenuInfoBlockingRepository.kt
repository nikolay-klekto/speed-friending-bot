package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.MenuInfo
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class MenuInfoBlockingRepository(
    private val dsl: DSLContext
) {

    fun getMenuInfoById(menuId: Int): MenuInfo {
        return dsl.select(MENU_INFO.MENU_ID, MENU_INFO.DESCRIPTION).from(MENU_INFO)
            .where(MENU_INFO.MENU_ID.eq(menuId))
            .first()
            .map { it.into(MenuInfo::class.java) }
    }

    fun getAllMenuIds(): List<Int> {
        return dsl.select(MENU_INFO.MENU_ID)
            .from(MENU_INFO)
            .map{it.into(Int::class.java)}
    }

    fun getAllMenuModels(): List<MenuInfo>{
        return dsl.select(MENU_INFO.asterisk())
            .from(MENU_INFO)
            .map { it.into(MenuInfo::class.java) }
    }
}