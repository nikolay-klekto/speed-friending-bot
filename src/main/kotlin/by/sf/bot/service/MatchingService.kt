package by.sf.bot.service

import by.sf.bot.jooq.tables.pojos.RandomCoffee
import by.sf.bot.models.Match
import by.sf.bot.repository.impl.RandomCoffeeRepository
import by.sf.bot.repository.impl.RandomCoffeeVariantsRepository
import org.springframework.stereotype.Service

@Service
class MatchingService(
    private val randomCoffeeRepository: RandomCoffeeRepository,
    private val randomCoffeeVariantsRepository: RandomCoffeeVariantsRepository
    ) {

    fun findMatches(userId: Int): List<Match> {
        val user = randomCoffeeRepository.getRandomCoffeeModelById(userId)
        val allUsers = randomCoffeeRepository.getAllRandomCoffeeAccountsBlock().filter { it.userId != userId }

        return allUsers.map { otherUser ->
            val compatibility = calculateCompatibility(user, otherUser)
            Match(otherUser.userId!!, compatibility)
        }.sortedByDescending { it.compatibility }
    }

    private fun calculateCompatibility(user1: RandomCoffee, user2: RandomCoffee): Double {
        var compatibility = 0.0

        // Calculate age compatibility (40%)
        val ageCompatibility = if (randomCoffeeVariantsRepository.getAgeRange(user1.idNote!!) ==
            randomCoffeeVariantsRepository.getAgeRange(user2.idNote!!)) 40.0 else 0.0
        compatibility += ageCompatibility

        // Calculate occupation compatibility (10%)
        val occupationCompatibility = if (randomCoffeeVariantsRepository.getOccupation(user1.idNote!!) ==
            randomCoffeeVariantsRepository.getOccupation(user2.idNote!!)) 10.0 else 0.0
        compatibility += occupationCompatibility

        // Calculate hobby compatibility (25%)
        val user1Hobbies = randomCoffeeVariantsRepository.getHobbies(user1.idNote!!)
        val user2Hobbies = randomCoffeeVariantsRepository.getHobbies(user2.idNote!!)
        val commonHobbies = user1Hobbies.intersect(user2Hobbies.toSet()).size
        val totalHobbies = user1Hobbies.union(user2Hobbies.toSet()).size
        val hobbyCompatibility = if (totalHobbies > 0) (commonHobbies.toDouble() / totalHobbies) * 25.0 else 0.0
        compatibility += hobbyCompatibility

        // Calculate places to visit compatibility (25%)
        val user1Places = randomCoffeeVariantsRepository.getPlaces(user1.idNote!!)
        val user2Places = randomCoffeeVariantsRepository.getPlaces(user2.idNote!!)
        val commonPlaces = user1Places.intersect(user2Places.toSet()).size
        val totalPlaces = user1Places.union(user2Places.toSet()).size
        val placesCompatibility = if (totalPlaces > 0) (commonPlaces.toDouble() / totalPlaces) * 25.0 else 0.0
        compatibility += placesCompatibility

        return compatibility
    }
}
