package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Ages.Companion.AGES
import by.sf.bot.jooq.tables.Hobbies.Companion.HOBBIES
import by.sf.bot.jooq.tables.Occupations.Companion.OCCUPATIONS
import by.sf.bot.jooq.tables.PlacesToVisit.Companion.PLACES_TO_VISIT
import by.sf.bot.jooq.tables.RandomCoffeeAge.Companion.RANDOM_COFFEE_AGE
import by.sf.bot.jooq.tables.RandomCoffeeHobby.Companion.RANDOM_COFFEE_HOBBY
import by.sf.bot.jooq.tables.RandomCoffeeOccupation.Companion.RANDOM_COFFEE_OCCUPATION
import by.sf.bot.jooq.tables.RandomCoffeePlace.Companion.RANDOM_COFFEE_PLACE
import by.sf.bot.jooq.tables.pojos.RandomCoffeeAge
import by.sf.bot.jooq.tables.pojos.RandomCoffeeHobby
import by.sf.bot.jooq.tables.pojos.RandomCoffeeOccupation
import by.sf.bot.jooq.tables.pojos.RandomCoffeePlace
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class RandomCoffeeVariantsRepository(
    private val dsl: DSLContext
) {

    fun getAllAgeVariants(): List<String> {
        return dsl.select(AGES.AGE_RANGE).from(AGES)
            .map { it.into(String::class.java) }
    }

    fun getAllOccupationsVariants(): List<String> {
        return dsl.select(OCCUPATIONS.OCCUPATION).from(OCCUPATIONS)
            .map { it.into(String::class.java) }
    }

    fun getAllHobbyVariants(): List<String> {
        return dsl.select(HOBBIES.HOBBY).from(HOBBIES)
            .map { it.into(String::class.java) }
    }

    fun getAllPlacesVariants(): List<String> {
        return dsl.select(PLACES_TO_VISIT.PLACE).from(PLACES_TO_VISIT)
            .map { it.into(String::class.java) }
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
        dsl.insertInto(RANDOM_COFFEE_AGE)
            .set(record)
            .execute()
//            .onDuplicateKeyUpdate()
//            .set(RANDOM_COFFEE_AGE.AGE_ID, record.ageId)
//            .execute()
    }

    fun saveCoffeeOccupation(randomCoffeeOccupation: RandomCoffeeOccupation) {
        val record = dsl.newRecord(RANDOM_COFFEE_OCCUPATION, randomCoffeeOccupation)
        dsl.insertInto(RANDOM_COFFEE_OCCUPATION)
            .set(record)
//            .onDuplicateKeyUpdate()
//            .set(RANDOM_COFFEE_OCCUPATION.OCCUPATION_ID, record.occupationId)
            .execute()
    }

    fun saveCoffeeHobby(randomCoffeeHobby: RandomCoffeeHobby) {
        val record = dsl.newRecord(RANDOM_COFFEE_HOBBY, randomCoffeeHobby)
        dsl.insertInto(RANDOM_COFFEE_HOBBY)
            .set(record)
//            .onDuplicateKeyUpdate()
//            .set(RANDOM_COFFEE_HOBBY.HOBBY_ID, record.hobbyId)
            .execute()
    }


fun saveCoffeePlace(randomCoffeePlace: RandomCoffeePlace) {
    val record = dsl.newRecord(RANDOM_COFFEE_PLACE, randomCoffeePlace)
    dsl.insertInto(RANDOM_COFFEE_PLACE)
        .set(record)
//        .onDuplicateKeyUpdate()
//        .set(RANDOM_COFFEE_HOBBY.HOBBY_ID, record.placeId)
        .execute()
        }

fun getAgeRange(randomCoffeeId: Int): String {
    return dsl.select(AGES.AGE_RANGE)
        .from(RANDOM_COFFEE_AGE)
        .join(AGES).on(RANDOM_COFFEE_AGE.AGE_ID.eq(AGES.AGE_ID))
        .where(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID.eq(randomCoffeeId))
        .first()
        .map { it.into(String::class.java) }
}

fun getOccupation(randomCoffeeId: Int): String {
    return dsl.select(OCCUPATIONS.OCCUPATION)
        .from(RANDOM_COFFEE_OCCUPATION)
        .join(OCCUPATIONS).on(RANDOM_COFFEE_OCCUPATION.OCCUPATION_ID.eq(OCCUPATIONS.OCCUPATION_ID))
        .where(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID.eq(randomCoffeeId))
        .first()
        .map { it.into(String::class.java) }
}

fun getHobbies(randomCoffeeId: Int): List<String> {
    return dsl.select(HOBBIES.HOBBY)
        .from(RANDOM_COFFEE_HOBBY)
        .join(HOBBIES).on(RANDOM_COFFEE_HOBBY.HOBBY_ID.eq(HOBBIES.HOBBY_ID))
        .where(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID.eq(randomCoffeeId))
        .map { it.into(String::class.java) }
}

fun getPlaces(randomCoffeeId: Int): List<String> {
    return dsl.select(PLACES_TO_VISIT.PLACE)
        .from(RANDOM_COFFEE_PLACE)
        .join(PLACES_TO_VISIT).on(RANDOM_COFFEE_PLACE.PLACE_ID.eq(PLACES_TO_VISIT.PLACE_ID))
        .where(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID.eq(randomCoffeeId))
        .map { it.into(String::class.java) }
}

    fun deleteAllVariantsByRandomCoffeeId(randomCoffeeId: Int){
        dsl.deleteFrom(RANDOM_COFFEE_AGE)
            .where(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID.eq(randomCoffeeId))
            .execute()

        dsl.deleteFrom(RANDOM_COFFEE_OCCUPATION)
            .where(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID.eq(randomCoffeeId))
            .execute()

        dsl.deleteFrom(RANDOM_COFFEE_HOBBY)
            .where(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID.eq(randomCoffeeId))
            .execute()

        dsl.deleteFrom(RANDOM_COFFEE_PLACE)
            .where(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID.eq(randomCoffeeId))
            .execute()
    }

    fun saveAllCoffeeHobbies(randomCoffeeHobbySet: Set<RandomCoffeeHobby>) {
            val records = randomCoffeeHobbySet.map { dsl.newRecord(RANDOM_COFFEE_HOBBY, it) }
            dsl.batchInsert(records).execute()
        }

    fun saveAllCoffeePlaces(randomCoffeePlacesSet: Set<RandomCoffeePlace>) {
        val records = randomCoffeePlacesSet.map { dsl.newRecord(RANDOM_COFFEE_PLACE, it) }
        dsl.batchInsert(records).execute()
    }


}