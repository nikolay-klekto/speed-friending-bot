/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.pojos


import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class Hobbies(
    var hobbyId: Int? = null,
    var hobby: String? = null,
    var freshStatus: Boolean? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("Hobbies (")

        sb.append(hobbyId)
        sb.append(", ").append(hobby)
        sb.append(", ").append(freshStatus)

        sb.append(")")
        return sb.toString()
    }
}
