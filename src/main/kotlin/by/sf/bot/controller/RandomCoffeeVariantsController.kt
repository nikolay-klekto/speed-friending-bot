package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.Ages
import by.sf.bot.jooq.tables.pojos.Hobbies
import by.sf.bot.jooq.tables.pojos.Occupations
import by.sf.bot.jooq.tables.pojos.PlacesToVisit
import by.sf.bot.repository.impl.RandomCoffeeVariantsRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class RandomCoffeeVariantsController(
    private val randomCoffeeVariantsRepository: RandomCoffeeVariantsRepository
) {
    @QueryMapping
    fun getAllAgeVariants(): Flux<Ages>{
        return randomCoffeeVariantsRepository.getAllAgeVariants()
    }

    @QueryMapping
    fun getAllOccupationsVariants(): Flux<Occupations>{
        return randomCoffeeVariantsRepository.getAllOccupationsVariants()
    }

    @QueryMapping
    fun getAllHobbyVariants(): Flux<Hobbies>{
        return randomCoffeeVariantsRepository.getAllHobbyVariants()
    }

    @QueryMapping
    fun getAllPlacesVariants(): Flux<PlacesToVisit>{
        return randomCoffeeVariantsRepository.getAllPlacesVariants()
    }

    @MutationMapping
    fun createAgeVariant(@Argument ageVariant: Ages): Mono<Boolean>{
        return randomCoffeeVariantsRepository.createAgeVariant(ageVariant)
    }

    @MutationMapping
    fun deleteAgeVariant(@Argument ageId: Int): Mono<Boolean>{
        return randomCoffeeVariantsRepository.deleteAgeVariant(ageId)
    }

    @MutationMapping
    fun createOccupationsVariant(@Argument occupationsVariant: Occupations): Mono<Boolean>{
        return randomCoffeeVariantsRepository.createOccupationsVariant(occupationsVariant)
    }

    @MutationMapping
    fun deleteOccupationsVariant(@Argument occupationsId: Int): Mono<Boolean>{
        return randomCoffeeVariantsRepository.deleteOccupationsVariant(occupationsId)
    }

    @MutationMapping
    fun createHobbyVariant(@Argument hobbyVariant: Hobbies): Mono<Boolean>{
        return randomCoffeeVariantsRepository.createHobbyVariant(hobbyVariant)
    }

    @MutationMapping
    fun deleteHobbyVariant(@Argument hobbyId: Int): Mono<Boolean>{
        return randomCoffeeVariantsRepository.deleteHobbyVariant(hobbyId)
    }

    @MutationMapping
    fun createPlaceVariant(@Argument placeVariant: PlacesToVisit): Mono<Boolean>{
        return randomCoffeeVariantsRepository.createPlaceVariant(placeVariant)
    }

    @MutationMapping
    fun deletePlaceVariant(@Argument placeId: Int): Mono<Boolean>{
        return randomCoffeeVariantsRepository.deletePlaceVariant(placeId)
    }

    @MutationMapping
    fun updateAgeVariantStatus(@Argument ageVariant: Ages): Mono<Boolean>{
        return randomCoffeeVariantsRepository.updateAgeVariantStatus(ageVariant)
    }

    @MutationMapping
    fun updateOccupationVariantStatus(@Argument occupationsVariant: Occupations): Mono<Boolean>{
        return randomCoffeeVariantsRepository.updateOccupationVariantStatus(occupationsVariant)
    }

    @MutationMapping
    fun updateHobbyVariantStatus(@Argument hobbyVariant: Hobbies): Mono<Boolean>{
        return randomCoffeeVariantsRepository.updateHobbyVariantStatus(hobbyVariant)
    }

    @MutationMapping
    fun updatePlaceVariantStatus(@Argument placeVariant: PlacesToVisit): Mono<Boolean>{
        return randomCoffeeVariantsRepository.updatePlaceVariantStatus(placeVariant)
    }

}