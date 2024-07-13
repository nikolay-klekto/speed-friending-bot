/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.records


import by.sf.bot.jooq.tables.Buttons

import java.time.LocalDate

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record7
import org.jooq.Row7
import org.jooq.impl.UpdatableRecordImpl


/**
 * Таблица для хранения информации о кнопках меню
 */
@Suppress("UNCHECKED_CAST")
open class ButtonsRecord() : UpdatableRecordImpl<ButtonsRecord>(Buttons.BUTTONS), Record7<Int?, Int?, String?, String?, String?, LocalDate?, Int?> {

    var buttonId: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    var menuId: Int?
        set(value): Unit = set(1, value)
        get(): Int? = get(1) as Int?

    var label: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    var actionType: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    var actionData: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    var dateCreated: LocalDate?
        set(value): Unit = set(5, value)
        get(): LocalDate? = get(5) as LocalDate?

    var position: Int?
        set(value): Unit = set(6, value)
        get(): Int? = get(6) as Int?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row7<Int?, Int?, String?, String?, String?, LocalDate?, Int?> = super.fieldsRow() as Row7<Int?, Int?, String?, String?, String?, LocalDate?, Int?>
    override fun valuesRow(): Row7<Int?, Int?, String?, String?, String?, LocalDate?, Int?> = super.valuesRow() as Row7<Int?, Int?, String?, String?, String?, LocalDate?, Int?>
    override fun field1(): Field<Int?> = Buttons.BUTTONS.BUTTON_ID
    override fun field2(): Field<Int?> = Buttons.BUTTONS.MENU_ID
    override fun field3(): Field<String?> = Buttons.BUTTONS.LABEL
    override fun field4(): Field<String?> = Buttons.BUTTONS.ACTION_TYPE
    override fun field5(): Field<String?> = Buttons.BUTTONS.ACTION_DATA
    override fun field6(): Field<LocalDate?> = Buttons.BUTTONS.DATE_CREATED
    override fun field7(): Field<Int?> = Buttons.BUTTONS.POSITION
    override fun component1(): Int? = buttonId
    override fun component2(): Int? = menuId
    override fun component3(): String? = label
    override fun component4(): String? = actionType
    override fun component5(): String? = actionData
    override fun component6(): LocalDate? = dateCreated
    override fun component7(): Int? = position
    override fun value1(): Int? = buttonId
    override fun value2(): Int? = menuId
    override fun value3(): String? = label
    override fun value4(): String? = actionType
    override fun value5(): String? = actionData
    override fun value6(): LocalDate? = dateCreated
    override fun value7(): Int? = position

    override fun value1(value: Int?): ButtonsRecord {
        this.buttonId = value
        return this
    }

    override fun value2(value: Int?): ButtonsRecord {
        this.menuId = value
        return this
    }

    override fun value3(value: String?): ButtonsRecord {
        this.label = value
        return this
    }

    override fun value4(value: String?): ButtonsRecord {
        this.actionType = value
        return this
    }

    override fun value5(value: String?): ButtonsRecord {
        this.actionData = value
        return this
    }

    override fun value6(value: LocalDate?): ButtonsRecord {
        this.dateCreated = value
        return this
    }

    override fun value7(value: Int?): ButtonsRecord {
        this.position = value
        return this
    }

    override fun values(value1: Int?, value2: Int?, value3: String?, value4: String?, value5: String?, value6: LocalDate?, value7: Int?): ButtonsRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        return this
    }

    /**
     * Create a detached, initialised ButtonsRecord
     */
    constructor(buttonId: Int? = null, menuId: Int? = null, label: String? = null, actionType: String? = null, actionData: String? = null, dateCreated: LocalDate? = null, position: Int? = null): this() {
        this.buttonId = buttonId
        this.menuId = menuId
        this.label = label
        this.actionType = actionType
        this.actionData = actionData
        this.dateCreated = dateCreated
        this.position = position
    }

    /**
     * Create a detached, initialised ButtonsRecord
     */
    constructor(value: by.sf.bot.jooq.tables.pojos.Buttons?): this() {
        if (value != null) {
            this.buttonId = value.buttonId
            this.menuId = value.menuId
            this.label = value.label
            this.actionType = value.actionType
            this.actionData = value.actionData
            this.dateCreated = value.dateCreated
            this.position = value.position
        }
    }
}
