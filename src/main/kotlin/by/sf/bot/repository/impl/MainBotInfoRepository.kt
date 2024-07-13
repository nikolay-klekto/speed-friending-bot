package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.MainBotInfo.Companion.MAIN_BOT_INFO
import by.sf.bot.jooq.tables.pojos.MainBotInfo
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
open class MainBotInfoRepository(
    private val dsl: DSLContext
) {

    fun save(mainBotInfo: MainBotInfo): Mono<MainBotInfo> {
        return Mono.fromSupplier {
            val newMainBotInfoRecord = dsl.newRecord(MAIN_BOT_INFO)
            newMainBotInfoRecord.from(mainBotInfo)
            newMainBotInfoRecord.reset(MAIN_BOT_INFO.ID_INFO)
            newMainBotInfoRecord.store()
            return@fromSupplier newMainBotInfoRecord.into(MainBotInfo::class.java)
        }
    }

    fun getMainBotInfoByKey(key: String): Mono<MainBotInfo>{
        return Mono.fromSupplier {
            dsl.select(MAIN_BOT_INFO.asterisk())
                .from(MAIN_BOT_INFO)
                .where(MAIN_BOT_INFO.KEY.eq(key))
                .firstOrNull()
                ?.map { it.into(MainBotInfo::class.java) }
        }
    }

}