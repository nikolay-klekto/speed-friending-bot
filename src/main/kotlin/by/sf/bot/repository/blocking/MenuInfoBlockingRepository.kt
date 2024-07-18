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
        return dsl.select(MENU_INFO.MENU_ID, MENU_INFO.TITLE, MENU_INFO.DESCRIPTION).from(MENU_INFO)
            .where(MENU_INFO.MENU_ID.eq(menuId))
            .first()
            .map { it.into(MenuInfo::class.java) }
    }

    fun getMenuInfoByTitle(menuTitle: String): MenuInfo? {
        return dsl.select(MENU_INFO.MENU_ID, MENU_INFO.TITLE, MENU_INFO.DESCRIPTION).from(
            MENU_INFO
        )
            .where(MENU_INFO.TITLE.eq(menuTitle))
            .firstOrNull()
            ?.map { it.into(MenuInfo::class.java) }

    }

    fun getAllMenuTitles(): List<String> {
        return dsl.select(MENU_INFO.TITLE)
            .from(MENU_INFO)
            .map{it.into(String::class.java)}
    }

    fun getAllMenuModels(): List<MenuInfo>{
        return dsl.select(MENU_INFO.asterisk())
            .from(MENU_INFO)
            .map { it.into(MenuInfo::class.java) }
    }
}