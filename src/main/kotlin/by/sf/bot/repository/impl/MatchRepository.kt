package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.UserMatches.Companion.USER_MATCHES
import by.sf.bot.jooq.tables.pojos.UserMatches
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class MatchRepository(
    private val dsl: DSLContext
) {
    fun getAllUserMatches(): List<UserMatches> {
        return dsl.selectFrom(USER_MATCHES)
            .map { it.into(UserMatches::class.java) }

    }
}