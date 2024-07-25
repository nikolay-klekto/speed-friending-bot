package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import by.sf.bot.repository.impl.RandomCoffeeRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class RandomCoffeeController(
    private val randomCoffeeRepository: RandomCoffeeRepository
) {


    @QueryMapping
    fun getAllRandomCoffeeAccounts(): Flux<RandomCoffee> {
        return randomCoffeeRepository.getAllRandomCoffeeAccounts()
    }

    @MutationMapping
    open fun addRandomCoffeeModel(@Argument randomCoffee: RandomCoffee): Mono<RandomCoffee> {
        return randomCoffeeRepository.save(randomCoffee)
    }

    @MutationMapping
    open fun updateRandomCoffeeModel(@Argument randomCoffee: RandomCoffee): Mono<Boolean> {
        return randomCoffeeRepository.update(randomCoffee)
    }

    @MutationMapping
    open fun deleteRandomCoffeeModelById(@Argument idNote: Int): Mono<Boolean> {
        return randomCoffeeRepository.delete(idNote)
    }
}