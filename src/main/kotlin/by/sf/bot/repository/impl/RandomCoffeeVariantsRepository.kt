package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Ages.Companion.AGES
import by.sf.bot.jooq.tables.Hobbies.Companion.HOBBIES
import by.sf.bot.jooq.tables.Occupations.Companion.OCCUPATIONS
import by.sf.bot.jooq.tables.PlacesToVisit.Companion.PLACES_TO_VISIT
import by.sf.bot.jooq.tables.RandomCoffeeAge.Companion.RANDOM_COFFEE_AGE
import by.sf.bot.jooq.tables.RandomCoffeeHobby.Companion.RANDOM_COFFEE_HOBBY
import by.sf.bot.jooq.tables.RandomCoffeeOccupation.Companion.RANDOM_COFFEE_OCCUPATION
import by.sf.bot.jooq.tables.RandomCoffeePlace.Companion.RANDOM_COFFEE_PLACE
import by.sf.bot.jooq.tables.pojos.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class RandomCoffeeVariantsRepository(
    private val dsl: DSLContext
) {

    fun getAllAgeVariants(): List<String>{
        return dsl.select(AGES.AGE_RANGE).from(AGES)
            .map{it.into(String::class.java)}
    }

    fun getAllOccupationsVariants(): List<String>{
        return dsl.select(OCCUPATIONS.OCCUPATION).from(OCCUPATIONS)
            .map{it.into(String::class.java)}
    }

    fun getAllHobbyVariants(): List<String>{
        return dsl.select(HOBBIES.HOBBY).from(HOBBIES)
            .map{it.into(String::class.java)}
    }

    fun getAllPlacesVariants(): List<String>{
        return dsl.select(PLACES_TO_VISIT.PLACE).from(PLACES_TO_VISIT)
            .map{it.into(String::class.java)}
    }

    fun getAgeIdByRange(ageRange: String): Int? {
        return dsl.select(AGES.AGE_ID)
            .from(AGES)
            .where(AGES.AGE_RANGE.eq(ageRange))
            .fetchOneInto(Int::class.java)
    }

    fun getOccupationIdByName(occupation: String): Int? {
        return dsl.select(OCCUPATIONS.OCCUPATION_ID)
            .from(OCCUPATIONS)
            .where(OCCUPATIONS.OCCUPATION.eq(occupation))
            .fetchOneInto(Int::class.java)
    }

    fun getHobbyIdByName(hobby: String): Int? {
        return dsl.select(HOBBIES.HOBBY_ID)
            .from(HOBBIES)
            .where(HOBBIES.HOBBY.eq(hobby))
            .fetchOneInto(Int::class.java)
    }

    fun getPlaceIdByName(place: String): Int? {
        return dsl.select(PLACES_TO_VISIT.PLACE_ID)
            .from(PLACES_TO_VISIT)
            .where(PLACES_TO_VISIT.PLACE.eq(place))
            .fetchOneInto(Int::class.java)
    }

    fun saveCoffeeAge(randomCoffeeAge: RandomCoffeeAge) {
        val record = dsl.newRecord(RANDOM_COFFEE_AGE, randomCoffeeAge)
        record.store()
    }

    fun saveCoffeeOccupation(randomCoffeeOccupation: RandomCoffeeOccupation) {
        val record = dsl.newRecord(RANDOM_COFFEE_OCCUPATION, randomCoffeeOccupation)
        record.store()
    }

    fun saveCoffeeHobby(randomCoffeeHobby: RandomCoffeeHobby) {
        val record = dsl.newRecord(RANDOM_COFFEE_HOBBY, randomCoffeeHobby)
        record.store()
    }

    fun saveCoffeePlace(randomCoffeePlace: RandomCoffeePlace) {
        val record = dsl.newRecord(RANDOM_COFFEE_PLACE, randomCoffeePlace)
        record.store()
    }

}