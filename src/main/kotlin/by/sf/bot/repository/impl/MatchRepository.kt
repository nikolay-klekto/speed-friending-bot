package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.UserMatches.Companion.USER_MATCHES
import by.sf.bot.jooq.tables.pojos.UserMatches
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
open class MatchRepository(
    private val dsl: DSLContext
) {

    fun saveUserMatches(userId: Int, compatibleUsers: List<Int>, viewedUsers: List<Int>) {
        val compatibleUsersString = compatibleUsers.joinToString(",")
        val viewedUsersString = viewedUsers.joinToString(",")

        val userMatchesRecord = dsl.newRecord(USER_MATCHES)
        userMatchesRecord.userId = userId
        userMatchesRecord.compatibleUsers = compatibleUsersString
        userMatchesRecord.viewedUsers = viewedUsersString

        dsl.insertInto(USER_MATCHES)
            .set(userMatchesRecord)
            .onDuplicateKeyUpdate()
            .set(USER_MATCHES.COMPATIBLE_USERS, compatibleUsersString)
            .set(USER_MATCHES.VIEWED_USERS, viewedUsersString)
            .execute()
    }

    fun getUserMatches(userId: Int): Pair<List<Int>, List<Int>> {
        val record = dsl.selectFrom(USER_MATCHES)
            .where(USER_MATCHES.USER_ID.eq(userId))
            .fetchOne()

        val compatibleUsers = record?.compatibleUsers?.split(",")?.map { it.toInt() } ?: emptyList()
        val viewedUsers = record?.viewedUsers?.split(",")?.map { it.toInt() } ?: emptyList()

        return Pair(compatibleUsers, viewedUsers)
    }

    fun getAllUserMatches(): List<UserMatches>{
       return dsl.selectFrom(USER_MATCHES)
           .map { it.into(UserMatches::class.java) }

    }

}