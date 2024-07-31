package by.sf.bot.service

import by.sf.bot.component.TelegramBot
import by.sf.bot.jooq.tables.UserMatches.Companion.USER_MATCHES
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import by.sf.bot.models.Match
import by.sf.bot.repository.blocking.RandomCoffeeBlockingRepository
import by.sf.bot.repository.impl.RandomCoffeeVariantsRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MatchingService(
    private val randomCoffeeVariantsRepository: RandomCoffeeVariantsRepository,
    private val randomCoffeeBlockingRepository: RandomCoffeeBlockingRepository,
    private val dsl: DSLContext
    ) {



    fun saveAllMatchesInDB() {
        TelegramBot.userMatchesMap.forEach { (userId, matches) ->
            val compatibleUsersString = matches.compatibleUsers.joinToString(",")
            val viewedUsersString = matches.viewedUsers.joinToString(",")

            dsl.insertInto(USER_MATCHES)
                .set(USER_MATCHES.USER_ID, userId)
                .set(USER_MATCHES.COMPATIBLE_USERS, compatibleUsersString)
                .set(USER_MATCHES.VIEWED_USERS, viewedUsersString)
                .onDuplicateKeyUpdate()
                .set(USER_MATCHES.COMPATIBLE_USERS, compatibleUsersString)
                .set(USER_MATCHES.VIEWED_USERS, viewedUsersString)
                .execute()
        }
    }

    fun findMatches(userId: Int): List<Match> {
        val randomCoffeeIdNote = randomCoffeeBlockingRepository.getRandomCoffeeModelIdNoteByUserId(userId)
        val allUsers = randomCoffeeBlockingRepository.getAllRandomCoffeeAccounts().filter { it.userId != userId }

        return allUsers.map { otherUser ->
            val compatibility = calculateCompatibility(randomCoffeeIdNote, otherUser)
            Match(otherUser.userId!!, compatibility)
        }.sortedByDescending { it.compatibility }
    }

    private fun calculateCompatibility(user1IdNote: Int, user2: RandomCoffee): Double {
        var compatibility = 0.0

        // Calculate age compatibility (40%)
        val ageCompatibility = if (randomCoffeeVariantsRepository.getAgeRange(user1IdNote) ==
            randomCoffeeVariantsRepository.getAgeRange(user2.idNote!!)) 40.0 else 0.0
        compatibility += ageCompatibility

        // Calculate occupation compatibility (10%)
        val occupationCompatibility = if (randomCoffeeVariantsRepository.getOccupation(user1IdNote) ==
            randomCoffeeVariantsRepository.getOccupation(user2.idNote!!)) 10.0 else 0.0
        compatibility += occupationCompatibility

        // Calculate hobby compatibility (25%)
        val user1Hobbies = randomCoffeeVariantsRepository.getHobbies(user1IdNote)
        val user2Hobbies = randomCoffeeVariantsRepository.getHobbies(user2.idNote!!)
        val commonHobbies = user1Hobbies.intersect(user2Hobbies.toSet()).size
        val totalHobbies = user1Hobbies.union(user2Hobbies.toSet()).size
        val hobbyCompatibility = if (totalHobbies > 0) (commonHobbies.toDouble() / totalHobbies) * 25.0 else 0.0
        compatibility += hobbyCompatibility

        // Calculate places to visit compatibility (25%)
        val user1Places = randomCoffeeVariantsRepository.getPlaces(user1IdNote)
        val user2Places = randomCoffeeVariantsRepository.getPlaces(user2.idNote!!)
        val commonPlaces = user1Places.intersect(user2Places.toSet()).size
        val totalPlaces = user1Places.union(user2Places.toSet()).size
        val placesCompatibility = if (totalPlaces > 0) (commonPlaces.toDouble() / totalPlaces) * 25.0 else 0.0
        compatibility += placesCompatibility

        return compatibility
    }
}
