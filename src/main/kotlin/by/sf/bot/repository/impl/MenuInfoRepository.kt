package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.MenuInfo.Companion.MENU_INFO
import by.sf.bot.jooq.tables.pojos.MainBotInfo
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.references.BUTTONS
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import org.jooq.DSLContext
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
//@CacheConfig(cacheNames = ["menuInfo"])
class MenuInfoRepository(
    private val menuInfoBlockingRepository: MenuInfoBlockingRepository,
    private val dsl: DSLContext
) {
//    @Cacheable(key = "#menuId", sync = true)
    fun getMenuInfo(menuId: Int): Mono<MenuInfo?> {
        return Mono.fromSupplier {
            dsl.select(MENU_INFO.MENU_ID, MENU_INFO.TITLE, MENU_INFO.DESCRIPTION).from(MENU_INFO)
                .where(MENU_INFO.MENU_ID.eq(menuId))
                .firstOrNull()
                ?.map { it.into(MenuInfo::class.java) }
        }
    }

    fun getMenuInfoByTitle(menuTitle: String): Mono<MenuInfo?> {
        return Mono.fromSupplier{
            menuInfoBlockingRepository.getMenuInfoByTitle(menuTitle)
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

    fun update(title: String, menuInfo: MenuInfo): Mono<Boolean> {
        return Mono.fromSupplier {
            val oldMenuModel: MenuInfo = menuInfoBlockingRepository.getMenuInfoByTitle(title)
                ?: return@fromSupplier false

            return@fromSupplier dsl.update(MENU_INFO)
                .set(MENU_INFO.TITLE, menuInfo.title ?: oldMenuModel.title)
                .set(MENU_INFO.DESCRIPTION, menuInfo.description ?: oldMenuModel.description)
                .set(MENU_INFO.PARENT_ID, menuInfo.parentId ?: oldMenuModel.parentId)
                .set(MENU_INFO.DATE_CREATED, LocalDate.now())
                .where(MENU_INFO.TITLE.eq(title))
                .execute() == 1
        }
    }

    fun delete(title: String): Mono<Boolean>{
        return Mono.fromSupplier {
            dsl.deleteFrom(BUTTONS)
                .where(BUTTONS.MENU_ID.eq(
                    dsl.select(MENU_INFO.MENU_ID)
                        .from(MENU_INFO)
                        .where(MENU_INFO.TITLE.eq(title))
                ))
                .execute()

            dsl.deleteFrom(MENU_INFO)
                .where(MENU_INFO.TITLE.eq(title))
                .execute() ==1
        }
    }
}