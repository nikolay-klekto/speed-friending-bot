package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.references.BUTTONS
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class MenuInfoRepository(
    private val menuInfoBlockingRepository: MenuInfoBlockingRepository,
    private val dsl: DSLContext
) {

    fun getAllMenuInfo(): Flux<MenuInfo>{
        return Flux.fromIterable(
            dsl.select(MENU_INFO.asterisk())
                .from(MENU_INFO)
                .map { it.into(MenuInfo::class.java) }
        )
    }
    fun getMenuInfo(menuId: Int): Mono<MenuInfo?> {
        return Mono.fromSupplier {
            dsl.select(MENU_INFO.MENU_ID, MENU_INFO.DESCRIPTION).from(MENU_INFO)
                .where(MENU_INFO.MENU_ID.eq(menuId))
                .firstOrNull()
                ?.map { it.into(MenuInfo::class.java) }
        }
    }

    fun save(menuInfo: MenuInfo): Mono<MenuInfo> {
        return Mono.fromSupplier {
            menuInfo.dateCreated = LocalDate.now()
            val newMenuInfoRecord = dsl.newRecord(MENU_INFO)
            newMenuInfoRecord.from(menuInfo)
            newMenuInfoRecord.reset(MENU_INFO.MENU_ID)
            newMenuInfoRecord.store()
            return@fromSupplier newMenuInfoRecord.into(MenuInfo::class.java)
        }
    }

    fun update(menuInfo: MenuInfo): Mono<Boolean> {
        return Mono.fromSupplier {
            val oldMenuModel: MenuInfo = menuInfoBlockingRepository.getMenuInfoById(menuInfo.menuId!!)

            return@fromSupplier dsl.update(MENU_INFO)
                .set(MENU_INFO.DESCRIPTION, menuInfo.description ?: oldMenuModel.description)
                .set(MENU_INFO.PARENT_ID, menuInfo.parentId ?: oldMenuModel.parentId)
                .set(MENU_INFO.DATE_CREATED, LocalDate.now())
                .where(MENU_INFO.MENU_ID.eq(menuInfo.menuId))
                .execute() == 1
        }
    }

    fun delete(menuId: Int): Mono<Boolean> {
        return Mono.fromSupplier {
            dsl.deleteFrom(BUTTONS)
                .where(BUTTONS.MENU_ID.eq(menuId))
                .execute()

            dsl.deleteFrom(MENU_INFO)
                .where(MENU_INFO.MENU_ID.eq(menuId))
                .execute() == 1
        }
    }
}