/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq.tables.records


import by.sf.bot.jooq.tables.PlacesToVisit

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class PlacesToVisitRecord() : UpdatableRecordImpl<PlacesToVisitRecord>(PlacesToVisit.PLACES_TO_VISIT), Record3<Int?, String?, Boolean?> {

    var placeId: Int?
        set(value): Unit = set(0, value)
        get(): Int? = get(0) as Int?

    var place: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    var freshStatus: Boolean?
        set(value): Unit = set(2, value)
        get(): Boolean? = get(2) as Boolean?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Int?> = super.key() as Record1<Int?>

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row3<Int?, String?, Boolean?> = super.fieldsRow() as Row3<Int?, String?, Boolean?>
    override fun valuesRow(): Row3<Int?, String?, Boolean?> = super.valuesRow() as Row3<Int?, String?, Boolean?>
    override fun field1(): Field<Int?> = PlacesToVisit.PLACES_TO_VISIT.PLACE_ID
    override fun field2(): Field<String?> = PlacesToVisit.PLACES_TO_VISIT.PLACE
    override fun field3(): Field<Boolean?> = PlacesToVisit.PLACES_TO_VISIT.FRESH_STATUS
    override fun component1(): Int? = placeId
    override fun component2(): String? = place
    override fun component3(): Boolean? = freshStatus
    override fun value1(): Int? = placeId
    override fun value2(): String? = place
    override fun value3(): Boolean? = freshStatus

    override fun value1(value: Int?): PlacesToVisitRecord {
        this.placeId = value
        return this
    }

    override fun value2(value: String?): PlacesToVisitRecord {
        this.place = value
        return this
    }

    override fun value3(value: Boolean?): PlacesToVisitRecord {
        this.freshStatus = value
        return this
    }

    override fun values(value1: Int?, value2: String?, value3: Boolean?): PlacesToVisitRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    /**
     * Create a detached, initialised PlacesToVisitRecord
     */
    constructor(placeId: Int? = null, place: String? = null, freshStatus: Boolean? = null): this() {
        this.placeId = placeId
        this.place = place
        this.freshStatus = freshStatus
    }

    /**
     * Create a detached, initialised PlacesToVisitRecord
     */
    constructor(value: by.sf.bot.jooq.tables.pojos.PlacesToVisit?): this() {
        if (value != null) {
            this.placeId = value.placeId
            this.place = value.place
            this.freshStatus = value.freshStatus
        }
    }
}
