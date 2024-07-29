package by.sf.bot.models

data class FullUserDataModel(
    var name: String? = "",
    var telegramUsername: String? = "",
    var age: String? = "",
    var occupation: String? = "",
    var hobbies: MutableList<String> = mutableListOf(),
    var visit: MutableList<String> = mutableListOf()
)