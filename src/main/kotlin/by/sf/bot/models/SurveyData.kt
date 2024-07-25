package by.sf.bot.models

data class SurveyData(
    var name: String = "",
    var age: String = "",
    var occupation: String = "",
    var hobbies: MutableList<String> = mutableListOf(),
    var visit: MutableList<String> = mutableListOf()
)