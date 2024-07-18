package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.MainBotInfo
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.repository.impl.MenuInfoRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
class MenuInfoController(
    private val menuInfoService: MenuInfoRepository
) {

    @QueryMapping
    fun getMenuInfoById(@Argument menuId: Int): Mono<MenuInfo?> {
        return menuInfoService.getMenuInfo(menuId)
    }

    @MutationMapping
    open fun addMenuInfo(@Argument menuInfo: MenuInfo): Mono<MenuInfo> {
        return menuInfoService.save(menuInfo)
    }

    @MutationMapping
    open fun updateMenuInfo(
        @Argument menuInfo: MenuInfo
    ): Mono<Boolean> {
        return menuInfoService.update(menuInfo)
    }

    @MutationMapping
    open fun deleteMenuInfoById(@Argument menuId: Int): Mono<Boolean> {
        return menuInfoService.delete(menuId)
    }
}