/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.pojos


import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class MainBotInfo(
    var idInfo: Int? = null,
    var key: String? = null,
    var value: String? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("MainBotInfo (")

        sb.append(idInfo)
        sb.append(", ").append(key)
        sb.append(", ").append(value)

        sb.append(")")
        return sb.toString()
    }
}
