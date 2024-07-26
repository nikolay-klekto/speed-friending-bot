/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.records


import by.sf.bot.jooq.tables.RandomCoffeeAge

import org.jooq.Field
import org.jooq.Record2
import org.jooq.Row2
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class RandomCoffeeAgeRecord() : UpdatableRecordImpl<RandomCoffeeAgeRecord>(RandomCoffeeAge.RANDOM_COFFEE_AGE), Record2<Int?, Int?> {

    var randomCoffeeId: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    var ageId: Int?
        set(value): Unit = set(1, value)
        get(): Int? = get(1) as Int?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record2<Int?, Int?> = super.key() as Record2<Int?, Int?>

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row2<Int?, Int?> = super.fieldsRow() as Row2<Int?, Int?>
    override fun valuesRow(): Row2<Int?, Int?> = super.valuesRow() as Row2<Int?, Int?>
    override fun field1(): Field<Int?> = RandomCoffeeAge.RANDOM_COFFEE_AGE.RANDOM_COFFEE_ID
    override fun field2(): Field<Int?> = RandomCoffeeAge.RANDOM_COFFEE_AGE.AGE_ID
    override fun component1(): Int? = randomCoffeeId
    override fun component2(): Int? = ageId
    override fun value1(): Int? = randomCoffeeId
    override fun value2(): Int? = ageId

    override fun value1(value: Int?): RandomCoffeeAgeRecord {
        this.randomCoffeeId = value
        return this
    }

    override fun value2(value: Int?): RandomCoffeeAgeRecord {
        this.ageId = value
        return this
    }

    override fun values(value1: Int?, value2: Int?): RandomCoffeeAgeRecord {
        this.value1(value1)
        this.value2(value2)
        return this
    }

    /**
     * Create a detached, initialised RandomCoffeeAgeRecord
     */
    constructor(randomCoffeeId: Int? = null, ageId: Int? = null): this() {
        this.randomCoffeeId = randomCoffeeId
        this.ageId = ageId
    }

    /**
     * Create a detached, initialised RandomCoffeeAgeRecord
     */
    constructor(value: by.sf.bot.jooq.tables.pojos.RandomCoffeeAge?): this() {
        if (value != null) {
            this.randomCoffeeId = value.randomCoffeeId
            this.ageId = value.ageId
        }
    }
}
