/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.pojos


import java.io.Serializable


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class RandomCoffeeHobby(
    var randomCoffeeId: Int? = null,
    var hobbyId: Int? = null
): Serializable {


    override fun toString(): String {
        val sb = StringBuilder("RandomCoffeeHobby (")

        sb.append(randomCoffeeId)
        sb.append(", ").append(hobbyId)

        sb.append(")")
        return sb.toString()
    }
}
