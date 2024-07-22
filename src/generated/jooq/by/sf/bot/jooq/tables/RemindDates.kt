/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.REMIND_DATES_PKEY
import by.sf.bot.jooq.keys.REMIND_DATES__REMIND_DATES_EVENT_ID_FKEY
import by.sf.bot.jooq.tables.records.RemindDatesRecord

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
open class RemindDates(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, RemindDatesRecord>?,
    aliased: Table<RemindDatesRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<RemindDatesRecord>(
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
         * The reference instance of <code>public.remind_dates</code>
         */
        val REMIND_DATES: RemindDates = RemindDates()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RemindDatesRecord> = RemindDatesRecord::class.java

    /**
     * The column <code>public.remind_dates.id</code>.
     */
    val ID: TableField<RemindDatesRecord, Int?> = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.remind_dates.description</code>.
     */
    val DESCRIPTION: TableField<RemindDatesRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "")

    /**
     * The column <code>public.remind_dates.event_id</code>.
     */
    val EVENT_ID: TableField<RemindDatesRecord, Int?> = createField(DSL.name("event_id"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.remind_dates.remind_date</code>.
     */
    val REMIND_DATE: TableField<RemindDatesRecord, LocalDate?> = createField(DSL.name("remind_date"), SQLDataType.LOCALDATE, this, "")

    /**
     * The column <code>public.remind_dates.date_created</code>.
     */
    val DATE_CREATED: TableField<RemindDatesRecord, LocalDate?> = createField(DSL.name("date_created"), SQLDataType.LOCALDATE, this, "")

    private constructor(alias: Name, aliased: Table<RemindDatesRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<RemindDatesRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.remind_dates</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.remind_dates</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.remind_dates</code> table reference
     */
    constructor(): this(DSL.name("remind_dates"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, RemindDatesRecord>): this(Internal.createPathAlias(child, key), child, key, REMIND_DATES, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<RemindDatesRecord, Int?> = super.getIdentity() as Identity<RemindDatesRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<RemindDatesRecord> = REMIND_DATES_PKEY
    override fun getReferences(): List<ForeignKey<RemindDatesRecord, *>> = listOf(REMIND_DATES__REMIND_DATES_EVENT_ID_FKEY)

    private lateinit var _events: Events

    /**
     * Get the implicit join path to the <code>public.events</code> table.
     */
    fun events(): Events {
        if (!this::_events.isInitialized)
            _events = Events(this, REMIND_DATES__REMIND_DATES_EVENT_ID_FKEY)

        return _events;
    }
    override fun `as`(alias: String): RemindDates = RemindDates(DSL.name(alias), this)
    override fun `as`(alias: Name): RemindDates = RemindDates(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): RemindDates = RemindDates(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): RemindDates = RemindDates(name, null)

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row5<Int?, String?, Int?, LocalDate?, LocalDate?> = super.fieldsRow() as Row5<Int?, String?, Int?, LocalDate?, LocalDate?>
}
