package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.Ages.Companion.AGES
import by.sf.bot.jooq.tables.Hobbies.Companion.HOBBIES
import by.sf.bot.jooq.tables.Occupations.Companion.OCCUPATIONS
import by.sf.bot.jooq.tables.PlacesToVisit.Companion.PLACES_TO_VISIT
import by.sf.bot.jooq.tables.RandomCoffee.Companion.RANDOM_COFFEE
import by.sf.bot.jooq.tables.RandomCoffeeAge.Companion.RANDOM_COFFEE_AGE
import by.sf.bot.jooq.tables.RandomCoffeeHobby.Companion.RANDOM_COFFEE_HOBBY
import by.sf.bot.jooq.tables.RandomCoffeeOccupation.Companion.RANDOM_COFFEE_OCCUPATION
import by.sf.bot.jooq.tables.RandomCoffeePlace.Companion.RANDOM_COFFEE_PLACE
import by.sf.bot.jooq.tables.UserMatches.Companion.USER_MATCHES
import by.sf.bot.jooq.tables.Users.Companion.USERS
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import by.sf.bot.models.FullUserDataModel
import by.sf.bot.service.AsyncMatchingService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class RandomCoffeeRepository(
    private val dsl: DSLContext,
    private val asyncMatchingService: AsyncMatchingService
) {

    fun getFullUserData(userId: Int): FullUserDataModel? {
        val result = dsl.select(
            RANDOM_COFFEE.USERNAME,
            RANDOM_COFFEE.TELEGRAM_USERNAME,
            AGES.AGE_RANGE,
            OCCUPATIONS.OCCUPATION,
            DSL.field("STRING_AGG(DISTINCT ${HOBBIES.HOBBY}, ',')").`as`("hobbies"),
            DSL.field("STRING_AGG(DISTINCT ${PLACES_TO_VISIT.PLACE}, ',')").`as`("visit")
        )
            .from(RANDOM_COFFEE)
            .leftJoin(RANDOM_COFFEE_AGE).on(RANDOM_COFFEE.ID_NOTE.eq(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID))
            .leftJoin(AGES).on(RANDOM_COFFEE_AGE.AGE_ID.eq(AGES.AGE_ID))
            .leftJoin(RANDOM_COFFEE_OCCUPATION).on(RANDOM_COFFEE.ID_NOTE.eq(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID))
            .leftJoin(OCCUPATIONS).on(RANDOM_COFFEE_OCCUPATION.OCCUPATION_ID.eq(OCCUPATIONS.OCCUPATION_ID))
            .leftJoin(RANDOM_COFFEE_HOBBY).on(RANDOM_COFFEE.ID_NOTE.eq(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID))
            .leftJoin(HOBBIES).on(RANDOM_COFFEE_HOBBY.HOBBY_ID.eq(HOBBIES.HOBBY_ID))
            .leftJoin(RANDOM_COFFEE_PLACE).on(RANDOM_COFFEE.ID_NOTE.eq(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID))
            .leftJoin(PLACES_TO_VISIT).on(RANDOM_COFFEE_PLACE.PLACE_ID.eq(PLACES_TO_VISIT.PLACE_ID))
            .where(RANDOM_COFFEE.USER_ID.eq(userId))
            .groupBy(RANDOM_COFFEE.USERNAME, RANDOM_COFFEE.TELEGRAM_USERNAME, AGES.AGE_RANGE, OCCUPATIONS.OCCUPATION)
            .fetchOne() ?: return null

        return FullUserDataModel(
            name = result.getValue(RANDOM_COFFEE.USERNAME),
            telegramUsername = result.getValue(RANDOM_COFFEE.TELEGRAM_USERNAME),
            age = result.getValue(AGES.AGE_RANGE),
            occupation = result.getValue(OCCUPATIONS.OCCUPATION),
            hobbies = result.getValue("hobbies")?.toString()?.split(",")?.toMutableList() ?: mutableListOf(),
            visit = result.getValue("visit")?.toString()?.split(",")?.toMutableList() ?: mutableListOf()
        )
    }

    fun getIdNoteByChatId(chatId: Long): Int? {
        return dsl.select(RANDOM_COFFEE.ID_NOTE).from(
            RANDOM_COFFEE
        ).where(
            RANDOM_COFFEE.USER_ID.eq(
                dsl.select(USERS.USER_ID).from(USERS)
                    .where(USERS.TELEGRAM_ID.eq(chatId))
            )
        ).firstOrNull()
            ?.map { it.into(Int::class.java) }
    }


    fun getRandomCoffeeModelById(userId: Int): RandomCoffee {
        return dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
            .where(RANDOM_COFFEE.USER_ID.eq(userId))
            .first()
            .map { it.into(RandomCoffee::class.java) }
    }

    fun getAllRandomCoffeeAccounts(): Flux<RandomCoffee> {
        return Flux.from {
            dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
                .map { it.into(RandomCoffee::class.java) }
        }
    }

    fun isRandomCoffeeModelExist(userId: Int): Boolean {
        return dsl.selectCount().from(RANDOM_COFFEE)
            .where(RANDOM_COFFEE.USER_ID.eq(userId))
            .map { it.into(Int::class.java) }
            .first() > 0
    }

    fun save(randomCoffeeModel: RandomCoffee): Mono<RandomCoffee> {
        return Mono.fromSupplier {
            randomCoffeeModel.dateCreated = LocalDate.now()
            val randomCoffeeRecord = dsl.newRecord(RANDOM_COFFEE)
            randomCoffeeRecord.from(randomCoffeeModel)
            randomCoffeeRecord.reset(RANDOM_COFFEE.ID_NOTE)
            randomCoffeeRecord.store()
            return@fromSupplier randomCoffeeRecord.into(RandomCoffee::class.java)
        }
    }

    fun saveBlock(randomCoffeeModel: RandomCoffee): RandomCoffee {
        randomCoffeeModel.dateCreated = LocalDate.now()
        val randomCoffeeRecord = dsl.newRecord(RANDOM_COFFEE)
        randomCoffeeRecord.from(randomCoffeeModel)
        randomCoffeeRecord.reset(RANDOM_COFFEE.ID_NOTE)
        randomCoffeeRecord.store()
        return randomCoffeeRecord.into(RandomCoffee::class.java)

    }

    fun update(randomCoffeeModel: RandomCoffee): Mono<Boolean> {
        return Mono.fromSupplier {
            val oldRandomCoffeeModel: RandomCoffee = getRandomCoffeeModelById(randomCoffeeModel.userId!!)

            return@fromSupplier dsl.update(RANDOM_COFFEE)
                .set(RANDOM_COFFEE.USERNAME, randomCoffeeModel.username ?: oldRandomCoffeeModel.username)
                .set(RANDOM_COFFEE.DATE_CREATED, LocalDate.now())
                .where(RANDOM_COFFEE.USER_ID.eq(randomCoffeeModel.userId))
                .execute() == 1
        }
    }

    fun updateBlock(randomCoffeeModel: RandomCoffee): Int {

        val oldRandomCoffeeModel: RandomCoffee = getRandomCoffeeModelById(randomCoffeeModel.userId!!)

        val result = dsl.update(RANDOM_COFFEE)
            .set(RANDOM_COFFEE.USERNAME, randomCoffeeModel.username ?: oldRandomCoffeeModel.username)
            .set(
                RANDOM_COFFEE.TELEGRAM_USERNAME,
                randomCoffeeModel.telegramUsername ?: oldRandomCoffeeModel.telegramUsername
            )
            .set(RANDOM_COFFEE.DATE_CREATED, LocalDate.now())
            .where(RANDOM_COFFEE.USER_ID.eq(randomCoffeeModel.userId))
            .execute() == 1

        if (result) {
            return oldRandomCoffeeModel.idNote!!
        } else throw Exception("Не удалось обновить random coffee с idNote: ${oldRandomCoffeeModel.idNote}")
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun delete(idNote: Int): Mono<Boolean> {
        return Mono.fromSupplier {

            val userId = dsl.select(RANDOM_COFFEE.USER_ID).from(RANDOM_COFFEE)
                .where(RANDOM_COFFEE.ID_NOTE.eq(idNote))
                .first()
                .map { it.into(Int::class.java) }

            val result1 = dsl.deleteFrom(RANDOM_COFFEE_AGE)
                .where(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID.eq(idNote))
                .execute() == 1

            val result2 = dsl.deleteFrom(RANDOM_COFFEE_HOBBY)
                .where(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result3 = dsl.deleteFrom(RANDOM_COFFEE_OCCUPATION)
                .where(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result4 = dsl.deleteFrom(RANDOM_COFFEE_PLACE)
                .where(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result5 = dsl.deleteFrom(USER_MATCHES)
                .where(USER_MATCHES.USER_ID.eq(userId))
                .execute()

            val result6 = dsl.deleteFrom(RANDOM_COFFEE)
                .where(RANDOM_COFFEE.ID_NOTE.eq(idNote))
                .execute() == 1

            GlobalScope.launch {
                asyncMatchingService.recalculateAllMatches()
            }



            return@fromSupplier result6 && result1
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteBlocking(idNote: Int):Boolean {


            val userId = dsl.select(RANDOM_COFFEE.USER_ID).from(RANDOM_COFFEE)
                .where(RANDOM_COFFEE.ID_NOTE.eq(idNote))
                .first()
                .map { it.into(Int::class.java) }

            val result1 = dsl.deleteFrom(RANDOM_COFFEE_AGE)
                .where(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID.eq(idNote))
                .execute() == 1

            val result2 = dsl.deleteFrom(RANDOM_COFFEE_HOBBY)
                .where(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result3 = dsl.deleteFrom(RANDOM_COFFEE_OCCUPATION)
                .where(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result4 = dsl.deleteFrom(RANDOM_COFFEE_PLACE)
                .where(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result5 = dsl.deleteFrom(USER_MATCHES)
                .where(USER_MATCHES.USER_ID.eq(userId))
                .execute()

            val result6 = dsl.deleteFrom(RANDOM_COFFEE)
                .where(RANDOM_COFFEE.ID_NOTE.eq(idNote))
                .execute() == 1

            GlobalScope.launch {
                asyncMatchingService.recalculateAllMatches()
            }

            return result6 && result1
        }

}