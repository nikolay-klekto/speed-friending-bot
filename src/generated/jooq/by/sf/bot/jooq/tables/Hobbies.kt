/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.HOBBIES_PKEY
import by.sf.bot.jooq.tables.records.HobbiesRecord

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row3
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
open class Hobbies(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, HobbiesRecord>?,
    aliased: Table<HobbiesRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<HobbiesRecord>(
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
         * The reference instance of <code>public.hobbies</code>
         */
        val HOBBIES: Hobbies = Hobbies()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<HobbiesRecord> = HobbiesRecord::class.java

    /**
     * The column <code>public.hobbies.hobby_id</code>.
     */
    val HOBBY_ID: TableField<HobbiesRecord, Int?> = createField(DSL.name("hobby_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.hobbies.hobby</code>.
     */
    val HOBBY: TableField<HobbiesRecord, String?> = createField(DSL.name("hobby"), SQLDataType.VARCHAR, this, "")

    /**
     * The column <code>public.hobbies.fresh_status</code>.
     */
    val FRESH_STATUS: TableField<HobbiesRecord, Boolean?> = createField(DSL.name("fresh_status"), SQLDataType.BOOLEAN.defaultValue(DSL.field("true", SQLDataType.BOOLEAN)), this, "")

    private constructor(alias: Name, aliased: Table<HobbiesRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<HobbiesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.hobbies</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.hobbies</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.hobbies</code> table reference
     */
    constructor(): this(DSL.name("hobbies"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, HobbiesRecord>): this(Internal.createPathAlias(child, key), child, key, HOBBIES, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<HobbiesRecord, Int?> = super.getIdentity() as Identity<HobbiesRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<HobbiesRecord> = HOBBIES_PKEY
    override fun `as`(alias: String): Hobbies = Hobbies(DSL.name(alias), this)
    override fun `as`(alias: Name): Hobbies = Hobbies(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Hobbies = Hobbies(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Hobbies = Hobbies(name, null)

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row3<Int?, String?, Boolean?> = super.fieldsRow() as Row3<Int?, String?, Boolean?>
}
