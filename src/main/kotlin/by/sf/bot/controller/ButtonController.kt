package by.sf.bot.controller

import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.repository.impl.ButtonRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ButtonController(
    private val buttonRepository: ButtonRepository
) {
    @QueryMapping
    fun getButtonsByMenuTitle(@Argument menuTitle: String): List<Buttons> {
        return buttonRepository.getAllButtonsByMenuTitle(menuTitle)
    }

//    @QueryMapping
//    fun getMenuInfoByTitle(@Argument title: String): Mono<MenuInfo?> {
//        return buttonRepository.getMenuInfoByTitle(title)
//    }

    @MutationMapping
    open fun addButton(@Argument button: Buttons): Mono<Buttons?> {
        return buttonRepository.save(button)
    }

    @MutationMapping
    open fun updateButton(
        @Argument menuTitle: String,
        @Argument label: String,
        @Argument button: Buttons
    ): Mono<Boolean> {
        return buttonRepository.update(menuTitle, label, button)
    }

    @MutationMapping
    open fun deleteButton(
        @Argument menuTitle: String,
        @Argument label: String
    ): Mono<Boolean> {
        return buttonRepository.delete(menuTitle, label)
    }
}