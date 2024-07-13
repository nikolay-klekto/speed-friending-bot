package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.MainBotInfo
import by.sf.bot.repository.impl.MainBotInfoRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
open class MainBotInfoController(
    private val mainBotInfoRepository: MainBotInfoRepository
) {

    @MutationMapping
    open fun addMainBotInfo(@Argument mainBotInfo: MainBotInfo): Mono<MainBotInfo>{
        return mainBotInfoRepository.save(mainBotInfo)
    }

    @QueryMapping
    open fun getMainBotInfoByKey(@Argument key: String): Mono<MainBotInfo>{
        return mainBotInfoRepository.getMainBotInfoByKey(key)
    }
}