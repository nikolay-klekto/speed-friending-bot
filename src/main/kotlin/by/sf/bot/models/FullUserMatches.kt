package by.sf.bot.models

data class FullUserMatches(
    var compatibleUsers: MutableList<Int> = mutableListOf(),
    var viewedUsers: MutableList<Int> = mutableListOf()
)