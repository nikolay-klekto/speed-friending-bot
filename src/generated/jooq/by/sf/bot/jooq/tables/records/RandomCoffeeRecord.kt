/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.records


import by.sf.bot.jooq.tables.RandomCoffee

import java.time.LocalDate

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record5
import org.jooq.Row5
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class RandomCoffeeRecord() : UpdatableRecordImpl<RandomCoffeeRecord>(RandomCoffee.RANDOM_COFFEE), Record5<Int?, Int?, String?, LocalDate?, String?> {

    var idNote: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    var userId: Int?
        set(value): Unit = set(1, value)
        get(): Int? = get(1) as Int?

    var username: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    var dateCreated: LocalDate?
        set(value): Unit = set(3, value)
        get(): LocalDate? = get(3) as LocalDate?

    var telegramUsername: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row5<Int?, Int?, String?, LocalDate?, String?> = super.fieldsRow() as Row5<Int?, Int?, String?, LocalDate?, String?>
    override fun valuesRow(): Row5<Int?, Int?, String?, LocalDate?, String?> = super.valuesRow() as Row5<Int?, Int?, String?, LocalDate?, String?>
    override fun field1(): Field<Int?> = RandomCoffee.RANDOM_COFFEE.ID_NOTE
    override fun field2(): Field<Int?> = RandomCoffee.RANDOM_COFFEE.USER_ID
    override fun field3(): Field<String?> = RandomCoffee.RANDOM_COFFEE.USERNAME
    override fun field4(): Field<LocalDate?> = RandomCoffee.RANDOM_COFFEE.DATE_CREATED
    override fun field5(): Field<String?> = RandomCoffee.RANDOM_COFFEE.TELEGRAM_USERNAME
    override fun component1(): Int? = idNote
    override fun component2(): Int? = userId
    override fun component3(): String? = username
    override fun component4(): LocalDate? = dateCreated
    override fun component5(): String? = telegramUsername
    override fun value1(): Int? = idNote
    override fun value2(): Int? = userId
    override fun value3(): String? = username
    override fun value4(): LocalDate? = dateCreated
    override fun value5(): String? = telegramUsername

    override fun value1(value: Int?): RandomCoffeeRecord {
        this.idNote = value
        return this
    }

    override fun value2(value: Int?): RandomCoffeeRecord {
        this.userId = value
        return this
    }

    override fun value3(value: String?): RandomCoffeeRecord {
        this.username = value
        return this
    }

    override fun value4(value: LocalDate?): RandomCoffeeRecord {
        this.dateCreated = value
        return this
    }

    override fun value5(value: String?): RandomCoffeeRecord {
        this.telegramUsername = value
        return this
    }

    override fun values(value1: Int?, value2: Int?, value3: String?, value4: LocalDate?, value5: String?): RandomCoffeeRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        return this
    }

    /**
     * Create a detached, initialised RandomCoffeeRecord
     */
    constructor(idNote: Int? = null, userId: Int? = null, username: String? = null, dateCreated: LocalDate? = null, telegramUsername: String? = null): this() {
        this.idNote = idNote
        this.userId = userId
        this.username = username
        this.dateCreated = dateCreated
        this.telegramUsername = telegramUsername
    }

    /**
     * Create a detached, initialised RandomCoffeeRecord
     */
    constructor(value: by.sf.bot.jooq.tables.pojos.RandomCoffee?): this() {
        if (value != null) {
            this.idNote = value.idNote
            this.userId = value.userId
            this.username = value.username
            this.dateCreated = value.dateCreated
            this.telegramUsername = value.telegramUsername
        }
    }
}
