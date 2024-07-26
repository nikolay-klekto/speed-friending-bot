/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.RANDOM_COFFEE_PLACE_PKEY
import by.sf.bot.jooq.keys.RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_PLACE_ID_FKEY
import by.sf.bot.jooq.keys.RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_RANDOM_COFFEE_ID_FKEY
import by.sf.bot.jooq.tables.records.RandomCoffeePlaceRecord

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row2
import org.jooq.Schema
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class RandomCoffeePlace(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, RandomCoffeePlaceRecord>?,
    aliased: Table<RandomCoffeePlaceRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<RandomCoffeePlaceRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.random_coffee_place</code>
         */
        val RANDOM_COFFEE_PLACE: RandomCoffeePlace = RandomCoffeePlace()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RandomCoffeePlaceRecord> = RandomCoffeePlaceRecord::class.java

    /**
     * The column <code>public.random_coffee_place.random_coffee_id</code>.
     */
    val RANDOM_COFFEE_ID: TableField<RandomCoffeePlaceRecord, Int?> = createField(DSL.name("random_coffee_id"), SQLDataType.INTEGER.nullable(false), this, "")

    /**
     * The column <code>public.random_coffee_place.place_id</code>.
     */
    val PLACE_ID: TableField<RandomCoffeePlaceRecord, Int?> = createField(DSL.name("place_id"), SQLDataType.INTEGER.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<RandomCoffeePlaceRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<RandomCoffeePlaceRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.random_coffee_place</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.random_coffee_place</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.random_coffee_place</code> table reference
     */
    constructor(): this(DSL.name("random_coffee_place"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, RandomCoffeePlaceRecord>): this(Internal.createPathAlias(child, key), child, key, RANDOM_COFFEE_PLACE, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<RandomCoffeePlaceRecord> = RANDOM_COFFEE_PLACE_PKEY
    override fun getReferences(): List<ForeignKey<RandomCoffeePlaceRecord, *>> = listOf(RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_RANDOM_COFFEE_ID_FKEY, RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_PLACE_ID_FKEY)

    private lateinit var _randomCoffee: RandomCoffee
    private lateinit var _placesToVisit: PlacesToVisit

    /**
     * Get the implicit join path to the <code>public.random_coffee</code>
     * table.
     */
    fun randomCoffee(): RandomCoffee {
        if (!this::_randomCoffee.isInitialized)
            _randomCoffee = RandomCoffee(this, RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_RANDOM_COFFEE_ID_FKEY)

        return _randomCoffee;
    }

    /**
     * Get the implicit join path to the <code>public.places_to_visit</code>
     * table.
     */
    fun placesToVisit(): PlacesToVisit {
        if (!this::_placesToVisit.isInitialized)
            _placesToVisit = PlacesToVisit(this, RANDOM_COFFEE_PLACE__RANDOM_COFFEE_PLACE_PLACE_ID_FKEY)

        return _placesToVisit;
    }
    override fun `as`(alias: String): RandomCoffeePlace = RandomCoffeePlace(DSL.name(alias), this)
    override fun `as`(alias: Name): RandomCoffeePlace = RandomCoffeePlace(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): RandomCoffeePlace = RandomCoffeePlace(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): RandomCoffeePlace = RandomCoffeePlace(name, null)

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row2<Int?, Int?> = super.fieldsRow() as Row2<Int?, Int?>
}
