package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.repository.impl.ButtonRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ButtonController(
    private val buttonRepository: ButtonRepository
) {

    @QueryMapping
    fun getAllButtonsInfo(): Flux<Buttons> {
        return buttonRepository.getAllButtonsInfo()
    }

    @QueryMapping
    fun getButtonsByMenuId(@Argument menuId: Int): List<Buttons> {
        return buttonRepository.getAllButtonsByMenuId(menuId)
    }

    @MutationMapping
    open fun addButton(@Argument button: Buttons): Mono<Buttons?> {
        return buttonRepository.save(button)
    }

    @MutationMapping
    open fun updateButton(
        @Argument menuId: Int,
        @Argument label: String,
        @Argument button: Buttons
    ): Mono<Boolean> {
        return buttonRepository.update(menuId, label, button)
    }

    @MutationMapping
    open fun deleteButton(
        @Argument menuId: Int,
        @Argument label: String
    ): Mono<Boolean> {
        return buttonRepository.delete(menuId, label)
    }
}