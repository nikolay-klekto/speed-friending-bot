package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.Users
import by.sf.bot.repository.impl.UserRepository
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class UsersController(
    private val userRepository: UserRepository
) {

    @QueryMapping
    fun getAllUsers(): Flux<Users>{
        return userRepository.getAllUsers()
    }

    @QueryMapping
    fun getUsersCountFromRandomCoffee(): Mono<Int> {
        return userRepository.getUsersCountFromRandomCoffee()
    }
}