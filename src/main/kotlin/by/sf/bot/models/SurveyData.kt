package by.sf.bot.models

data class SurveyData(
    var name: String = "",
    var telegramUsername: String = "",
    var age: String = "",
    var occupation: String = "",
    var hobbies: MutableSet<String> = mutableSetOf(),
    var visit: MutableSet<String> = mutableSetOf()
)