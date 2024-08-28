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
data class Events(
    var eventId: Int? = null,
    var eventDate: LocalDate? = null,
    var eventDateText: String? = null,
    var dateCreated: LocalDate? = null,
    var googleFormUrl: String? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("Events (")

        sb.append(eventId)
        sb.append(", ").append(eventDate)
        sb.append(", ").append(eventDateText)
        sb.append(", ").append(dateCreated)
        sb.append(", ").append(googleFormUrl)

        sb.append(")")
        return sb.toString()
    }
}
