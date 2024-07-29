package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.RandomCoffee.Companion.RANDOM_COFFEE
import by.sf.bot.jooq.tables.RandomCoffeeAge.Companion.RANDOM_COFFEE_AGE
import by.sf.bot.jooq.tables.RandomCoffeeHobby.Companion.RANDOM_COFFEE_HOBBY
import by.sf.bot.jooq.tables.RandomCoffeeOccupation.Companion.RANDOM_COFFEE_OCCUPATION
import by.sf.bot.jooq.tables.RandomCoffeePlace.Companion.RANDOM_COFFEE_PLACE
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Repository
class RandomCoffeeRepository(
    private val dsl: DSLContext
) {

    fun getRandomCoffeeModelById(userId: Int): RandomCoffee {
        return dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
            .where(RANDOM_COFFEE.USER_ID.eq(userId))
            .first()
            .map { it.into(RandomCoffee::class.java) }
    }

    fun getAllRandomCoffeeAccountsBlock(): List<RandomCoffee> {
        return dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
                .map { it.into(RandomCoffee::class.java) }
    }

    fun getAllRandomCoffeeAccounts(): Flux<RandomCoffee> {
        return Flux.from {
            dsl.select(RANDOM_COFFEE.asterisk()).from(RANDOM_COFFEE)
                .map { it.into(RandomCoffee::class.java) }
        }
    }

    fun isRandomCoffeeModelExist(userId: Int): Boolean{
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

            val result =  dsl.update(RANDOM_COFFEE)
                .set(RANDOM_COFFEE.USERNAME, randomCoffeeModel.username ?: oldRandomCoffeeModel.username)
                .set(RANDOM_COFFEE.DATE_CREATED, LocalDate.now())
                .where(RANDOM_COFFEE.USER_ID.eq(randomCoffeeModel.userId))
                .execute() == 1

        if(result){
            return oldRandomCoffeeModel.idNote!!
        }else throw Exception("Не удалось обновить random coffee с idNote: ${oldRandomCoffeeModel.idNote}")
    }

    fun delete(idNote: Int): Mono<Boolean> {
        return Mono.fromSupplier {
            val result1  = dsl.deleteFrom(RANDOM_COFFEE_AGE)
                .where(RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID.eq(idNote))
                .execute() == 1

            val result2  = dsl.deleteFrom(RANDOM_COFFEE_HOBBY)
                .where(RANDOM_COFFEE_HOBBY.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result3  = dsl.deleteFrom(RANDOM_COFFEE_OCCUPATION)
                .where(RANDOM_COFFEE_OCCUPATION.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            val result4  = dsl.deleteFrom(RANDOM_COFFEE_PLACE)
                .where(RANDOM_COFFEE_PLACE.RANDOM_COFFEE_ID.eq(idNote))
                .execute()

            return@fromSupplier dsl.deleteFrom(RANDOM_COFFEE)
                .where(RANDOM_COFFEE.ID_NOTE.eq(idNote))
                .execute() == 1 && result1
        }
    }
}