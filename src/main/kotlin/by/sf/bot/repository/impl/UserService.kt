//package by.sf.bot.repository.impl
//
//import org.jooq.DSLContext
//import org.springframework.stereotype.Repository
//import reactor.core.publisher.Mono
//
//@Repository
//class UserService(private val dsl: DSLContext) {
//
//    fun saveUser(telegramId: Long, username: String) {
//        Mono.fromCallable{
//
//        }
//        dsl.insertInto(Tables.USER, Tables.USER.TELEGRAM_ID, Tables.USER.USERNAME)
//            .values(telegramId, username)
//            .execute()
//    }
//
//    fun findUserByTelegramId(telegramId: Long): UserRecord? {
//        return dsl.selectFrom(Tables.USER)
//            .where(Tables.USER.TELEGRAM_ID.eq(telegramId))
//            .fetchOne()
//    }
//}