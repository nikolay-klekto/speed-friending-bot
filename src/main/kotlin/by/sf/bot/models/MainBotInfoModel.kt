package by.sf.bot.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("main_bot_info")
data class MainBotInfoModel(
    @Id val idInfo: Int? = null,
    val key: String,
    val value: String
)
