package by.sf.bot.repository.impl

import by.sf.bot.jooq.tables.RandomCoffee.Companion.RANDOM_COFFEE
import by.sf.bot.jooq.tables.Users.Companion.USERS
import by.sf.bot.jooq.tables.pojos.Users
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserRepository(
    private val dsl: DSLContext
) {
    fun getAllUsers(): Flux<Users>{
        return Flux.from(
            dsl.select(USERS.asterisk()).from(USERS)
        ).map { it.into(Users::class.java) }
    }

    fun getUsersCountFromRandomCoffee(): Mono<Int>{
        return Mono.fromSupplier {
            dsl.selectCount().from(RANDOM_COFFEE)
                .first()
                .map { it.into(Int::class.java) }
        }
    }
}