/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.RANDOM_COFFEE_PKEY
import by.sf.bot.jooq.keys.RANDOM_COFFEE__FK_USER
import by.sf.bot.jooq.tables.records.RandomCoffeeRecord

import java.time.LocalDate

import kotlin.collections.List

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row5
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
open class RandomCoffee(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, RandomCoffeeRecord>?,
    aliased: Table<RandomCoffeeRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<RandomCoffeeRecord>(
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
         * The reference instance of <code>public.random_coffee</code>
         */
        val RANDOM_COFFEE: RandomCoffee = RandomCoffee()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RandomCoffeeRecord> = RandomCoffeeRecord::class.java

    /**
     * The column <code>public.random_coffee.id_note</code>.
     */
    val ID_NOTE: TableField<RandomCoffeeRecord, Int?> = createField(DSL.name("id_note"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.random_coffee.user_id</code>.
     */
    val USER_ID: TableField<RandomCoffeeRecord, Int?> = createField(DSL.name("user_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.random_coffee.username</code>.
     */
    val USERNAME: TableField<RandomCoffeeRecord, String?> = createField(DSL.name("username"), SQLDataType.VARCHAR, this, "")

    /**
     * The column <code>public.random_coffee.date_created</code>.
     */
    val DATE_CREATED: TableField<RandomCoffeeRecord, LocalDate?> = createField(DSL.name("date_created"), SQLDataType.LOCALDATE, this, "")

    /**
     * The column <code>public.random_coffee.telegram_username</code>.
     */
    val TELEGRAM_USERNAME: TableField<RandomCoffeeRecord, String?> = createField(DSL.name("telegram_username"), SQLDataType.VARCHAR, this, "")

    private constructor(alias: Name, aliased: Table<RandomCoffeeRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<RandomCoffeeRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.random_coffee</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.random_coffee</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.random_coffee</code> table reference
     */
    constructor(): this(DSL.name("random_coffee"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, RandomCoffeeRecord>): this(Internal.createPathAlias(child, key), child, key, RANDOM_COFFEE, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<RandomCoffeeRecord, Int?> = super.getIdentity() as Identity<RandomCoffeeRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<RandomCoffeeRecord> = RANDOM_COFFEE_PKEY
    override fun getReferences(): List<ForeignKey<RandomCoffeeRecord, *>> = listOf(RANDOM_COFFEE__FK_USER)

    private lateinit var _users: Users

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    fun users(): Users {
        if (!this::_users.isInitialized)
            _users = Users(this, RANDOM_COFFEE__FK_USER)

        return _users;
    }
    override fun `as`(alias: String): RandomCoffee = RandomCoffee(DSL.name(alias), this)
    override fun `as`(alias: Name): RandomCoffee = RandomCoffee(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): RandomCoffee = RandomCoffee(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): RandomCoffee = RandomCoffee(name, null)

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row5<Int?, Int?, String?, LocalDate?, String?> = super.fieldsRow() as Row5<Int?, Int?, String?, LocalDate?, String?>
}
