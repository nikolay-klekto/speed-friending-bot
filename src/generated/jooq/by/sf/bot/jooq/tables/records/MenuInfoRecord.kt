/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.records


import by.sf.bot.jooq.tables.MenuInfo

import java.time.LocalDate

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record5
import org.jooq.Row5
import org.jooq.impl.UpdatableRecordImpl


/**
 * Таблица для хранения информации о меню
 */
@Suppress("UNCHECKED_CAST")
open class MenuInfoRecord() : UpdatableRecordImpl<MenuInfoRecord>(MenuInfo.MENU_INFO), Record5<Int?, String?, String?, Int?, LocalDate?> {

    var menuId: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    var title: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    var description: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    var parentId: Int?
        set(value): Unit = set(3, value)
        get(): Int? = get(3) as Int?

    var dateCreated: LocalDate?
        set(value): Unit = set(4, value)
        get(): LocalDate? = get(4) as LocalDate?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row5<Int?, String?, String?, Int?, LocalDate?> = super.fieldsRow() as Row5<Int?, String?, String?, Int?, LocalDate?>
    override fun valuesRow(): Row5<Int?, String?, String?, Int?, LocalDate?> = super.valuesRow() as Row5<Int?, String?, String?, Int?, LocalDate?>
    override fun field1(): Field<Int?> = MenuInfo.MENU_INFO.MENU_ID
    override fun field2(): Field<String?> = MenuInfo.MENU_INFO.TITLE
    override fun field3(): Field<String?> = MenuInfo.MENU_INFO.DESCRIPTION
    override fun field4(): Field<Int?> = MenuInfo.MENU_INFO.PARENT_ID
    override fun field5(): Field<LocalDate?> = MenuInfo.MENU_INFO.DATE_CREATED
    override fun component1(): Int? = menuId
    override fun component2(): String? = title
    override fun component3(): String? = description
    override fun component4(): Int? = parentId
    override fun component5(): LocalDate? = dateCreated
    override fun value1(): Int? = menuId
    override fun value2(): String? = title
    override fun value3(): String? = description
    override fun value4(): Int? = parentId
    override fun value5(): LocalDate? = dateCreated

    override fun value1(value: Int?): MenuInfoRecord {
        this.menuId = value
        return this
    }

    override fun value2(value: String?): MenuInfoRecord {
        this.title = value
        return this
    }

    override fun value3(value: String?): MenuInfoRecord {
        this.description = value
        return this
    }

    override fun value4(value: Int?): MenuInfoRecord {
        this.parentId = value
        return this
    }

    override fun value5(value: LocalDate?): MenuInfoRecord {
        this.dateCreated = value
        return this
    }

    override fun values(value1: Int?, value2: String?, value3: String?, value4: Int?, value5: LocalDate?): MenuInfoRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        return this
    }

    /**
     * Create a detached, initialised MenuInfoRecord
     */
    constructor(menuId: Int? = null, title: String? = null, description: String? = null, parentId: Int? = null, dateCreated: LocalDate? = null): this() {
        this.menuId = menuId
        this.title = title
        this.description = description
        this.parentId = parentId
        this.dateCreated = dateCreated
    }

    /**
     * Create a detached, initialised MenuInfoRecord
     */
    constructor(value: by.sf.bot.jooq.tables.pojos.MenuInfo?): this() {
        if (value != null) {
            this.menuId = value.menuId
            this.title = value.title
            this.description = value.description
            this.parentId = value.parentId
            this.dateCreated = value.dateCreated
        }
    }
}
