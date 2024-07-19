/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.EVENT_INFO_PKEY
import by.sf.bot.jooq.tables.records.EventInfoRecord

import java.time.LocalDate

import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
import org.jooq.Name
import org.jooq.Record
import org.jooq.Row4
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
open class EventInfo(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, EventInfoRecord>?,
    aliased: Table<EventInfoRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<EventInfoRecord>(
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
         * The reference instance of <code>public.event_info</code>
         */
        val EVENT_INFO: EventInfo = EventInfo()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<EventInfoRecord> = EventInfoRecord::class.java

    /**
     * The column <code>public.event_info.event_id</code>.
     */
    val EVENT_ID: TableField<EventInfoRecord, Int?> = createField(DSL.name("event_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.event_info.button_id</code>.
     */
    val BUTTON_ID: TableField<EventInfoRecord, Int?> = createField(DSL.name("button_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.event_info.description</code>.
     */
    val DESCRIPTION: TableField<EventInfoRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "")

    /**
     * The column <code>public.event_info.date_created</code>.
     */
    val DATE_CREATED: TableField<EventInfoRecord, LocalDate?> = createField(DSL.name("date_created"), SQLDataType.LOCALDATE, this, "")

    private constructor(alias: Name, aliased: Table<EventInfoRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<EventInfoRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.event_info</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.event_info</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.event_info</code> table reference
     */
    constructor(): this(DSL.name("event_info"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, EventInfoRecord>): this(Internal.createPathAlias(child, key), child, key, EVENT_INFO, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<EventInfoRecord, Int?> = super.getIdentity() as Identity<EventInfoRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<EventInfoRecord> = EVENT_INFO_PKEY
    override fun `as`(alias: String): EventInfo = EventInfo(DSL.name(alias), this)
    override fun `as`(alias: Name): EventInfo = EventInfo(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): EventInfo = EventInfo(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): EventInfo = EventInfo(name, null)

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row4<Int?, Int?, String?, LocalDate?> = super.fieldsRow() as Row4<Int?, Int?, String?, LocalDate?>
}
