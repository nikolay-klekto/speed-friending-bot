/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables


import by.sf.bot.jooq.Public
import by.sf.bot.jooq.keys.REMINDERS_PKEY
import by.sf.bot.jooq.tables.records.RemindersRecord

import java.time.LocalDate

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
 * Таблица для хранения информации о напоминаниях
 */
@Suppress("UNCHECKED_CAST")
open class Reminders(
    alias: Name,
    child: Table<out Record>?,
    path: ForeignKey<out Record, RemindersRecord>?,
    aliased: Table<RemindersRecord>?,
    parameters: Array<Field<*>?>?
): TableImpl<RemindersRecord>(
    alias,
    Public.PUBLIC,
    child,
    path,
    aliased,
    parameters,
    DSL.comment("Таблица для хранения информации о напоминаниях"),
    TableOptions.table()
) {
    companion object {

        /**
         * The reference instance of <code>public.reminders</code>
         */
        val REMINDERS: Reminders = Reminders()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RemindersRecord> = RemindersRecord::class.java

    /**
     * The column <code>public.reminders.reminder_id</code>. Уникальный
     * идентификатор напоминания
     */
    val REMINDER_ID: TableField<RemindersRecord, Int?> = createField(DSL.name("reminder_id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "Уникальный идентификатор напоминания")

    /**
     * The column <code>public.reminders.title_description</code>. Краткое
     * описание напоминания
     */
    val TITLE_DESCRIPTION: TableField<RemindersRecord, String?> = createField(DSL.name("title_description"), SQLDataType.VARCHAR, this, "Краткое описание напоминания")

    /**
     * The column <code>public.reminders.description</code>. Полное описание
     * напоминания
     */
    val DESCRIPTION: TableField<RemindersRecord, String?> = createField(DSL.name("description"), SQLDataType.VARCHAR, this, "Полное описание напоминания")

    /**
     * The column <code>public.reminders.event_date</code>. Дата мероприятия, о
     * котором напоминается
     */
    val EVENT_DATE: TableField<RemindersRecord, LocalDate?> = createField(DSL.name("event_date"), SQLDataType.LOCALDATE, this, "Дата мероприятия, о котором напоминается")

    /**
     * The column <code>public.reminders.date_created</code>. Дата создания
     * записи
     */
    val DATE_CREATED: TableField<RemindersRecord, LocalDate?> = createField(DSL.name("date_created"), SQLDataType.LOCALDATE, this, "Дата создания записи")

    private constructor(alias: Name, aliased: Table<RemindersRecord>?): this(alias, null, null, aliased, null)
    private constructor(alias: Name, aliased: Table<RemindersRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, aliased, parameters)

    /**
     * Create an aliased <code>public.reminders</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.reminders</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.reminders</code> table reference
     */
    constructor(): this(DSL.name("reminders"), null)

    constructor(child: Table<out Record>, key: ForeignKey<out Record, RemindersRecord>): this(Internal.createPathAlias(child, key), child, key, REMINDERS, null)
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<RemindersRecord, Int?> = super.getIdentity() as Identity<RemindersRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<RemindersRecord> = REMINDERS_PKEY
    override fun `as`(alias: String): Reminders = Reminders(DSL.name(alias), this)
    override fun `as`(alias: Name): Reminders = Reminders(alias, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Reminders = Reminders(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Reminders = Reminders(name, null)

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------
    override fun fieldsRow(): Row5<Int?, String?, String?, LocalDate?, LocalDate?> = super.fieldsRow() as Row5<Int?, String?, String?, LocalDate?, LocalDate?>
}
