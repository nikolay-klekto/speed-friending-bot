package by.sf.bot.models

data class FullUserMatches(
    val compatibleUsers: MutableList<Int> = mutableListOf(),
    val viewedUsers: MutableList<Int> = mutableListOf()
)