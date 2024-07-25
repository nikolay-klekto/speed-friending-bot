/*
 * This file is generated by jOOQ.
 */
package by.sf.bot.jooq


import by.sf.bot.jooq.tables.Buttons
import by.sf.bot.jooq.tables.Events
import by.sf.bot.jooq.tables.MainBotInfo
import by.sf.bot.jooq.tables.MenuInfo
import by.sf.bot.jooq.tables.RandomCoffee
import by.sf.bot.jooq.tables.RemindDates
import by.sf.bot.jooq.tables.Users

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>public</code>
         */
        val PUBLIC: Public = Public()
    }

    /**
     * Таблица для хранения информации о кнопках меню
     */
    val BUTTONS: Buttons get() = Buttons.BUTTONS

    /**
     * The table <code>public.events</code>.
     */
    val EVENTS: Events get() = Events.EVENTS

    /**
     * The table <code>public.main_bot_info</code>.
     */
    val MAIN_BOT_INFO: MainBotInfo get() = MainBotInfo.MAIN_BOT_INFO

    /**
     * Таблица для хранения информации о меню
     */
    val MENU_INFO: MenuInfo get() = MenuInfo.MENU_INFO

    /**
     * The table <code>public.random_coffee</code>.
     */
    val RANDOM_COFFEE: RandomCoffee get() = RandomCoffee.RANDOM_COFFEE

    /**
     * The table <code>public.remind_dates</code>.
     */
    val REMIND_DATES: RemindDates get() = RemindDates.REMIND_DATES

    /**
     * Таблица для хранения информации о пользователях
     */
    val USERS: Users get() = Users.USERS

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        Buttons.BUTTONS,
        Events.EVENTS,
        MainBotInfo.MAIN_BOT_INFO,
        MenuInfo.MENU_INFO,
        RandomCoffee.RANDOM_COFFEE,
        RemindDates.REMIND_DATES,
        Users.USERS
    )
}
