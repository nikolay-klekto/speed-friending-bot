package by.sf.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("users")
data class UserModel(
    @Id val userId: Int? = null,
    val telegramId: Int,
    val username: String,
    val dateCreated: LocalDate,
    val remindStatus: Boolean
)