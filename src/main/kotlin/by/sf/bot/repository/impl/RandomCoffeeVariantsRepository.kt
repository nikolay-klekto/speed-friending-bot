package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Ages.Companion.AGES
import by.sf.bot.jooq.tables.Buttons
import by.sf.bot.jooq.tables.Hobbies.Companion.HOBBIES
import by.sf.bot.jooq.tables.Occupations.Companion.OCCUPATIONS
import by.sf.bot.jooq.tables.PlacesToVisit.Companion.PLACES_TO_VISIT
import by.sf.bot.jooq.tables.RandomCoffeeAge.Companion.RANDOM_COFFEE_AGE
import by.sf.bot.jooq.tables.RandomCoffeeHobby.Companion.RANDOM_COFFEE_HOBBY
import by.sf.bot.jooq.tables.RandomCoffeeOccupation.Companion.RANDOM_COFFEE_OCCUPATION
import by.sf.bot.jooq.tables.RandomCoffeePlace.Companion.RANDOM_COFFEE_PLACE
import by.sf.bot.jooq.tables.pojos.*
import org.jooq.DSLContext
import org.jooq.Table
import org.jooq.TableField
import org.jooq.impl.DSL
import org.jooq.impl.DSL.coalesce
import org.jooq.impl.DSL.min
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class RandomCoffeeVariantsRepository(
    private val dsl: DSLContext
) {

    fun getAllAgeVariantsBlocking(): List<Ages> {
        return dsl.select(AGES.asterisk()).from(AGES)
            .where(AGES.FRESH_STATUS.eq(true))
            .map { it.into(Ages::class.java) }
            .sortedBy { it.ageRange?.split("-")?.first()?.toIntOrNull() ?: Int.MAX_VALUE }
    }

    fun getAllAgeVariants(): Flux<Ages>{
        return Flux.fromIterable(
            dsl.select(AGES.asterisk()).from(AGES)
                .map { it.into(Ages::class.java) }
                .sortedBy { it.ageRange?.split("-")?.first()?.toIntOrNull() ?: Int.MAX_VALUE }
        )
    }

    fun createAgeVariant(ageVariant: Ages): Mono<Boolean>{
        return Mono.fromSupplier {
            val nextAgeId = getNextTableID(AGES, AGES.AGE_ID)
            ageVariant.ageId = nextAgeId
            val record = dsl.newRecord(AGES, ageVariant)

            return@fromSupplier dsl.insertInto(AGES).set(record)
                .execute() == 1
        }
    }

    fun deleteAgeVariant(ageId: Int): Mono<Boolean>{
        return Mono.fromSupplier {
            return@fromSupplier dsl.deleteFrom(AGES)
                .where(AGES.FRESH_STATUS.eq(false))
                .execute() == 1
        }
    }

    fun getAllOccupationsVariantsBlocking(): List<Occupations> {
        return dsl.select(OCCUPATIONS.asterisk()).from(OCCUPATIONS)
            .where(OCCUPATIONS.FRESH_STATUS.eq(true))
            .map { it.into(Occupations::class.java) }
    }

    fun getAllOccupationsVariants(): Flux<Occupations>{
        return Flux.fromIterable(
            dsl.select(OCCUPATIONS.asterisk()).from(OCCUPATIONS)
                .map { it.into(Occupations::class.java) }
        )
    }

    fun createOccupationsVariant(occupationsVariant: Occupations): Mono<Boolean>{
        return Mono.fromSupplier {
            val nextOccupationsId = getNextTableID(OCCUPATIONS, OCCUPATIONS.OCCUPATION_ID)
            occupationsVariant.occupationId = nextOccupationsId
            val record = dsl.newRecord(OCCUPATIONS, occupationsVariant)

            return@fromSupplier dsl.insertInto(OCCUPATIONS).set(record)
                .execute() == 1
        }
    }

    fun deleteOccupationsVariant(occupationsId: Int): Mono<Boolean>{
        return Mono.fromSupplier {
            return@fromSupplier dsl.deleteFrom(OCCUPATIONS)
                .where(OCCUPATIONS.FRESH_STATUS.eq(false))
                .execute() == 1
        }
    }

    fun getAllHobbyVariantsBlocking(): List<Hobbies> {
        return dsl.select(HOBBIES.asterisk()).from(HOBBIES)
            .where(HOBBIES.FRESH_STATUS.eq(true))
            .map { it.into(Hobbies::class.java) }
    }

    fun getAllHobbyVariants(): Flux<Hobbies>{
        return Flux.fromIterable(
            dsl.select(HOBBIES.asterisk()).from(HOBBIES)
                .map { it.into(Hobbies::class.java) }
        )
    }

    fun createHobbyVariant(hobbyVariant: Hobbies): Mono<Boolean>{
        return Mono.fromSupplier {
            val nextHobbyId = getNextTableID(HOBBIES, HOBBIES.HOBBY_ID)
            hobbyVariant.hobbyId = nextHobbyId
            val record = dsl.newRecord(HOBBIES, hobbyVariant)

            return@fromSupplier dsl.insertInto(HOBBIES).set(record)
                .execute() == 1
        }
    }

    fun deleteHobbyVariant(hobbyId: Int): Mono<Boolean>{
        return Mono.fromSupplier {
            return@fromSupplier dsl.deleteFrom(HOBBIES)
                .where(HOBBIES.FRESH_STATUS.eq(false))
                .execute() == 1
        }
    }

    fun getAllPlacesVariantsBlocking(): List<PlacesToVisit> {
        return dsl.select(PLACES_TO_VISIT.asterisk()).from(PLACES_TO_VISIT)
            .where(PLACES_TO_VISIT.FRESH_STATUS.eq(true))
            .map { it.into(PlacesToVisit::class.java) }
    }

    fun getAllPlacesVariants(): Flux<PlacesToVisit>{
        return Flux.fromIterable(
            dsl.select(PLACES_TO_VISIT.asterisk()).from(PLACES_TO_VISIT)
                .map { it.into(PlacesToVisit::class.java) }
        )
    }

    fun createPlaceVariant(placeVariant: PlacesToVisit): Mono<Boolean>{
        return Mono.fromSupplier {
            val nextPlaceId = getNextTableID(PLACES_TO_VISIT, PLACES_TO_VISIT.PLACE_ID)
            placeVariant.placeId= nextPlaceId
            val record = dsl.newRecord(PLACES_TO_VISIT, placeVariant)

            return@fromSupplier dsl.insertInto(PLACES_TO_VISIT).set(record)
                .execute() == 1
        }
    }

    fun deletePlaceVariant(placeId: Int): Mono<Boolean>{
        return Mono.fromSupplier {
            return@fromSupplier dsl.deleteFrom(PLACES_TO_VISIT)
                .where(PLACES_TO_VISIT.FRESH_STATUS.eq(false))
                .execute() == 1
        }
    }

    fun saveCoffeeAge(randomCoffeeAge: RandomCoffeeAge) {
        val record = dsl.newRecord(RANDOM_COFFEE_AGE, randomCoffeeAge)
        dsl.insertInto(RANDOM_COFFEE_AGE)
            .set(record)
            .execute()
    }

    fun saveCoffeeOccupation(randomCoffeeOccupation: RandomCoffeeOccupation) {
        val record = dsl.newRecord(RANDOM_COFFEE_OCCUPATION, randomCoffeeOccupation)
        dsl.insertInto(RANDOM_COFFEE_OCCUPATION)
            .set(record)
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

    fun deleteAllVariantsByRandomCoffeeId(randomCoffeeId: Int) {
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

    fun getNextTableID(table: Table<*>, idField: TableField<*, Int?>): Int? {
        val t1 = table.`as`("t1")
        val t2 = table.`as`("t2")

        return dsl.select(coalesce(min(t1.field(idField)?.add(1)), 1))
            .from(t1)
            .leftJoin(t2)
            .on(t1.field(idField)?.add(1)?.eq(t2.field(idField)))
            .where(t2.field(idField)?.isNull)
            .fetchOneInto(Int::class.java)
    }

    fun updateAgeVariantStatus(updateModel: Ages):Mono<Boolean>{
        return Mono.fromSupplier {
            dsl.update(AGES)
                .set(AGES.FRESH_STATUS, updateModel.freshStatus)
                .where(AGES.AGE_ID.eq(updateModel.ageId))
                .execute() == 1
        }
    }

    fun updateOccupationVariantStatus(updateModel: Occupations):Mono<Boolean>{
        return Mono.fromSupplier {
            dsl.update(OCCUPATIONS)
                .set(OCCUPATIONS.FRESH_STATUS, updateModel.freshStatus)
                .where(OCCUPATIONS.OCCUPATION_ID.eq(updateModel.occupationId))
                .execute() == 1
        }
    }

    fun updateHobbyVariantStatus(updateModel: Hobbies):Mono<Boolean>{
        return Mono.fromSupplier {
            dsl.update(HOBBIES)
                .set(HOBBIES.FRESH_STATUS, updateModel.freshStatus)
                .where(HOBBIES.HOBBY_ID.eq(updateModel.hobbyId))
                .execute() == 1
        }
    }

    fun updatePlaceVariantStatus(updateModel: PlacesToVisit):Mono<Boolean>{
        return Mono.fromSupplier {
            dsl.update(PLACES_TO_VISIT)
                .set(PLACES_TO_VISIT.FRESH_STATUS, updateModel.freshStatus)
                .where(PLACES_TO_VISIT.PLACE_ID.eq(updateModel.placeId))
                .execute() == 1
        }
    }


}