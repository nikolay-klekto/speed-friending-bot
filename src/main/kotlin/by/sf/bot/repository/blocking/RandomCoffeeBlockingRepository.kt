package by.sf.bot.repository.blocking

import by.sf.bot.jooq.tables.RandomCoffee.Companion.RANDOM_COFFEE
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class RandomCoffeeBlockingRepository(
    private val dsl: DSLContext
) {
    fun getAllRandomCoffeeAccounts(): List<RandomCoffee> {
        return dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
            .map { it.into(RandomCoffee::class.java) }
    }

    fun getAllRandomCoffeeAccountsUsersId(): List<Int> {
        return dsl.select(RANDOM_COFFEE.USER_ID).from(RANDOM_COFFEE)
            .map { it.into(Int::class.java) }
    }

    fun getRandomCoffeeModelIdNoteByUserId(userId: Int): Int {
        return dsl.select(RANDOM_COFFEE.ID_NOTE).from(RANDOM_COFFEE)
            .where(RANDOM_COFFEE.USER_ID.eq(userId))
            .first()
            .map { it.into(Int::class.java) }
    }

}