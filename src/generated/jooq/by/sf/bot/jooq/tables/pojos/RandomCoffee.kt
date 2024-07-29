/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.pojos


import java.io.Serializable
import java.time.LocalDate


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class RandomCoffee(
    var idNote: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var dateCreated: LocalDate? = null,
    var telegramUsername: String? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("RandomCoffee (")

        sb.append(idNote)
        sb.append(", ").append(userId)
        sb.append(", ").append(username)
        sb.append(", ").append(dateCreated)
        sb.append(", ").append(telegramUsername)

        sb.append(")")
        return sb.toString()
    }
}
