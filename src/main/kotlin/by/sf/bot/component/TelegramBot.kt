package by.sf.bot.component

import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.jooq.tables.pojos.RandomCoffee
import by.sf.bot.jooq.tables.pojos.Users
import by.sf.bot.models.SurveyData
import by.sf.bot.models.SurveyState
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import by.sf.bot.repository.blocking.UserBlockingRepository
import by.sf.bot.repository.impl.ButtonRepository
import by.sf.bot.repository.impl.MainBotInfoRepository
import by.sf.bot.repository.impl.RandomCoffeeRepository
import jakarta.annotation.PostConstruct
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.time.LocalDate


class TelegramBot(
    private val mainBotInfoRepository: MainBotInfoRepository,
    private val menuInfoBlockingRepository: MenuInfoBlockingRepository,
    private val buttonRepository: ButtonRepository,
    private val userBlockingRepository: UserBlockingRepository,
    private val randomCoffeeRepository: RandomCoffeeRepository
) : TelegramLongPollingBot() {

    private var botUsername: String = ""
    private var botToken: String = ""
    private var menuWithButtonsCollection: HashMap<Int, HashMap<Int, Buttons>> = hashMapOf()
    private var menuInfoList: List<MenuInfo> = listOf()

    @PostConstruct
    fun init() {
        botUsername = mainBotInfoRepository.getMainBotInfoByKey(BOT_USERNAME).block()?.value!!
        botToken = mainBotInfoRepository.getMainBotInfoByKey(BOT_TOKEN).block()?.value!!
        menuInfoList = menuInfoBlockingRepository.getAllMenuModels()

        val listMenuIds: List<Int> = menuInfoBlockingRepository.getAllMenuIds()

        listMenuIds.forEach { menuId ->
            val currentButtons: List<Buttons> = buttonRepository.getAllButtonsByMenuId(menuId)
            currentButtons.forEach { currentButton ->
                if (menuWithButtonsCollection.containsKey(menuId)) {
                    // Вставляем данные во вложенную карту
                    menuWithButtonsCollection[menuId]?.put(currentButton.position!!, currentButton)
                } else {
                    // Создаем новую вложенную карту и вставляем данные
                    val innerMap = HashMap<Int, Buttons>()
                    innerMap[currentButton.position!!] = currentButton
                    menuWithButtonsCollection[menuId] = innerMap
                }
            }

        }

        // Другие данные, которые нужно загрузить
    }

    fun loadDataFromDatabase() {

    }

    override fun getBotUsername(): String = botUsername
    override fun getBotToken(): String = botToken

    override fun onUpdateReceived(update: Update) {
        if (update.hasCallbackQuery()) {
            val callbackQuery = update.callbackQuery
            val callbackData = callbackQuery.data
            val callbackChatId = callbackQuery.message.chatId

            when (callbackData) {
                REMINDER_MESSAGE_ALL -> {
                    sendReminderOptions(callbackChatId, MESSAGE_ALL)
                }

                REMINDER_MESSAGE_DELETE -> {
                    sendReminderOptions(callbackChatId, null)
                }

                CALLBACK_DATA_MENU_ID_6 -> sendMenuInfo(callbackChatId, 6)
                CALLBACK_DATA_MENU_ID_7 -> sendMenuInfo(callbackChatId, 7)
                CALLBACK_DATA_MENU_ID_3 -> sendMenuInfo(callbackChatId, 3)
                CALLBACK_DATA_MENU_ID_9 -> sendMenuInfo(callbackChatId, 9)
                CALLBACK_DATA_MENU_ID_10 -> sendMenuInfo(callbackChatId, 10)
                CALLBACK_DATA_MENU_ID_11 -> sendMenuInfo(callbackChatId, 11)
                CALLBACK_DATA_MENU_ID_13 -> startSurvey(callbackChatId)

                else -> {
                    if(callbackData.startsWith(REMINDER_MESSAGE_YES)){
                        val reminders = callbackData.split(REMINDER_MESSAGE_DELIMITER)[1]
                        sendReminderOptions(callbackChatId, reminders)
                    }else{
                        handleCallbackQuery(callbackChatId, callbackData)
                    }
                }
            }
            return
        }

        val message = update.message
        val chatId = message.chatId

        val startButtons = menuWithButtonsCollection[START_PAGE_MENU_ID]

        when (message.text) {
            START_MESSAGE -> sendStartMessage(chatId)
            startButtons?.get(1)?.label -> sendMenuInfo(chatId, 2)
            startButtons?.get(2)?.label -> sendMenuInfo(chatId, 3)
            startButtons?.get(3)?.label -> sendMenuInfo(chatId, 4)
            startButtons?.get(4)?.label -> sendMenuInfo(chatId, 12)
            else -> handleUserResponse(chatId, message.text)
        }
    }

    fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage(chatId.toString(), text)
        execute(message)
    }

    private fun sendStartMessage(chatId: Long) {
        val currentMenuModel = menuInfoList.find { it.parentId == null }
        val currentButtonList = menuWithButtonsCollection[currentMenuModel?.menuId]

        val text = currentMenuModel!!.description!!
        val keyboardMarkup = ReplyKeyboardMarkup()
        val keyboard: MutableList<KeyboardRow> = ArrayList()

        val row1 = KeyboardRow()
        row1.add(currentButtonList?.get(1)!!.label)
        row1.add(currentButtonList[2]!!.label)

        val row2 = KeyboardRow()
        row2.add(currentButtonList[4]!!.label)

        val row3 = KeyboardRow()
        row3.add(currentButtonList[3]!!.label)


// Добавляем ряды в клавиатуру
        keyboard.add(row1)
        keyboard.add(row2)
        keyboard.add(row3)

        keyboardMarkup.keyboard = keyboard
        keyboardMarkup.resizeKeyboard = true

        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        message.replyMarkup = keyboardMarkup
        execute(message)
    }

    private fun sendMenuInfo(chatId: Long, menuId: Int) {
        val currentMenuModel = menuInfoList.find { it.menuId == menuId }
        val currentButtonList = menuWithButtonsCollection[currentMenuModel?.menuId]
        val text = currentMenuModel!!.description!!
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val keyboard: MutableList<List<InlineKeyboardButton>> = ArrayList()

        currentButtonList?.values?.forEach { button ->
            val inlineKeyboardButton = InlineKeyboardButton()
            inlineKeyboardButton.text = button.label!!

            // Добавляем проверку на тип действия и наличие данных
            when (button.actionType) {
                ACTION_TYPE_URL -> {
                    if (!button.actionData.isNullOrEmpty()) {
                        inlineKeyboardButton.url = button.actionData
                        keyboard.add(listOf(inlineKeyboardButton))
                    }
                }

                ACTION_TYPE_CALLBACK -> {
                    if (!button.actionData.isNullOrEmpty()) {
                        inlineKeyboardButton.callbackData = button.actionData
                        keyboard.add(listOf(inlineKeyboardButton))
                    }
                }
            }
        }

        if (keyboard.isNotEmpty()) {
            inlineKeyboardMarkup.keyboard = keyboard
        }
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text

// Устанавливаем разметку клавиатуры только если она есть
        if (keyboard.isNotEmpty()) {
            message.replyMarkup = inlineKeyboardMarkup
        }

        execute(message)
    }

    private fun sendReminderOptions(chatId: Long, reminders: String?) {

        val userExistStatus = userBlockingRepository.isUserExist(chatId)

        if (userExistStatus) {

            val updateStatus = userBlockingRepository.update(chatId, reminders)

            if (updateStatus) {
                if (reminders != null) {
                    sendMessage(chatId, REMIND_MESSAGE_STATUS_SUCCESS)
                } else sendMessage(chatId, REMIND_MESSAGE_STATUS_DELETE)

            } else sendMessage(chatId, REMIND_MESSAGE_STATUS_ERROR)

        } else if (reminders != null) {
            val newUser = Users(
                telegramId = chatId,
                dateCreated = LocalDate.now(),
                reminders = reminders
            )
            val saveStatus = userBlockingRepository.save(newUser)
            if (saveStatus) {
                sendMessage(chatId, REMIND_MESSAGE_STATUS_SUCCESS)
            } else sendMessage(chatId, REMIND_MESSAGE_STATUS_ERROR)
        } else sendMessage(chatId, REMIND_MESSAGE_STATUS_SUCCESS)
    }

    private fun startSurvey(chatId: Long) {
        userStates[chatId] = SurveyState.ASK_NAME
        userSurveyData[chatId] = SurveyData()

        sendMessage(chatId, RANDOM_COFFEE_INPUT_MESSAGE_NAME)
    }

    private fun handleUserResponse(chatId: Long, response: String) {
        val state = userStates[chatId] ?: return


        when (state) {
            SurveyState.ASK_NAME -> {
                userSurveyData[chatId]?.name = response
                userStates[chatId] = SurveyState.ASK_AGE
                sendAgeSelection(chatId)
            }

            SurveyState.ASK_AGE -> {
                userSurveyData[chatId]?.age = response
                userStates[chatId] = SurveyState.ASK_OCCUPATION
                sendOccupationSelection(chatId)
            }

            SurveyState.ASK_OCCUPATION -> {
                userSurveyData[chatId]?.occupation = response
                userStates[chatId] = SurveyState.ASK_HOBBIES
                sendHobbiesSelection(chatId)
            }

            SurveyState.ASK_HOBBIES -> {
                if (response == RANDOM_COFFEE_CALLBACK_MESSAGE_DONE_RUSSIA) {
                    userStates[chatId] = SurveyState.ASK_VISIT
                    sendVisitSelection(chatId)
                } else {
                    userSurveyData[chatId]?.hobbies?.add(response)
                    sendHobbiesSelection(chatId)
                }
            }

            SurveyState.ASK_VISIT -> {
                if (response == RANDOM_COFFEE_CALLBACK_MESSAGE_DONE) {
                    completeSurvey(chatId)
                } else {
                    userSurveyData[chatId]?.visit?.add(response)
                    sendVisitSelection(chatId) // Пользователь может выбрать еще одно место или нажать "Готово"
                }
            }
        }
    }

    private fun completeSurvey(chatId: Long) {
        val surveyData = userSurveyData[chatId]

        userStates.remove(chatId)
        if (!userBlockingRepository.isUserExist(chatId)) {
            userBlockingRepository.save(
                Users(
                    telegramId = chatId
                )
            )
        }
        val userId = userBlockingRepository.getUserIdByChatId(chatId)

        val newRandomCoffee = RandomCoffee(
            userId = userId,
            username = surveyData?.name,
            age = surveyData?.age,
            occupation = surveyData?.occupation,
            hobby = surveyData?.hobbies.toString(),
            wouldLikeToVisit = surveyData?.visit.toString()
        )
        if(!randomCoffeeRepository.isRandomCoffeeModelExist(userId)){
            randomCoffeeRepository.saveBlock(newRandomCoffee)
        }

        randomCoffeeRepository.updateBlock(newRandomCoffee)

        sendMessage(chatId, RANDOM_COFFEE_SAVING_FORM_SUCCESS)
    }

    private fun sendAgeSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = RANDOM_COFFEE_INPUT_MESSAGE_AGE

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val rows: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val ageCategories = listOf("18-21", "22-24", "25-100")
        ageCategories.forEach { age ->
            val button = InlineKeyboardButton()
            button.text = age
            button.callbackData = age

            val row: MutableList<InlineKeyboardButton> = ArrayList()
            row.add(button)
            rows.add(row)
        }

        inlineKeyboardMarkup.keyboard = rows
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun sendOccupationSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = RANDOM_COFFEE_INPUT_MESSAGE_OCCUPATION

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val rows: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val occupations = listOf(
            "Экономика и бизнес", "Наука и технологии", "Образование и культура",
            "Здравоохранение и медицина", "Гос управление и право", "Экология и сельское хозяйство"
        )
        occupations.forEach { occupation ->
            val button = InlineKeyboardButton()
            button.text = occupation
            button.callbackData = occupation

            val row: MutableList<InlineKeyboardButton> = ArrayList()
            row.add(button)
            rows.add(row)
        }

        inlineKeyboardMarkup.keyboard = rows
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun sendHobbiesSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = RANDOM_COFFEE_INPUT_MESSAGE_HOBBY

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val rows: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val hobbies = listOf("Спорт", "Настольные игры", "Книги")
        hobbies.forEach { hobby ->
            val button = InlineKeyboardButton()
            button.text = hobby
            button.callbackData = hobby

            val row: MutableList<InlineKeyboardButton> = ArrayList()
            row.add(button)
            rows.add(row)
        }

        // Добавляем кнопку "Готово"
        val doneButton = InlineKeyboardButton()
        doneButton.text = RANDOM_COFFEE_CALLBACK_MESSAGE_DONE_RUSSIA
        doneButton.callbackData = RANDOM_COFFEE_CALLBACK_MESSAGE_DONE
        val doneRow: MutableList<InlineKeyboardButton> = ArrayList()
        doneRow.add(doneButton)
        rows.add(doneRow)

        inlineKeyboardMarkup.keyboard = rows
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun sendVisitSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = RANDOM_COFFEE_INPUT_MESSAGE_WOULD_LIKE_TO_VISIT

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val rows: MutableList<List<InlineKeyboardButton>> = ArrayList()

        val visitOptions = listOf("Музей", "Кино", "Рок-фестиваль")
        visitOptions.forEach { option ->
            val button = InlineKeyboardButton()
            button.text = option
            button.callbackData = option

            val row: MutableList<InlineKeyboardButton> = ArrayList()
            row.add(button)
            rows.add(row)
        }

        // Добавляем кнопку "Готово"
        val doneButton = InlineKeyboardButton()
        doneButton.text = RANDOM_COFFEE_CALLBACK_MESSAGE_DONE_RUSSIA
        doneButton.callbackData = RANDOM_COFFEE_CALLBACK_MESSAGE_DONE
        val doneRow: MutableList<InlineKeyboardButton> = ArrayList()
        doneRow.add(doneButton)
        rows.add(doneRow)

        inlineKeyboardMarkup.keyboard = rows
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun handleCallbackQuery(chatId: Long, data: String) {
        val state = userStates[chatId] ?: return

        when (state) {
            SurveyState.ASK_AGE -> {
                userSurveyData[chatId]?.age = data
                userStates[chatId] = SurveyState.ASK_OCCUPATION
                sendOccupationSelection(chatId)
            }
            SurveyState.ASK_OCCUPATION -> {
                userSurveyData[chatId]?.occupation = data
                userStates[chatId] = SurveyState.ASK_HOBBIES
                sendHobbiesSelection(chatId)
            }
            SurveyState.ASK_HOBBIES -> {
                if (data == RANDOM_COFFEE_CALLBACK_MESSAGE_DONE) {
                    userStates[chatId] = SurveyState.ASK_VISIT
                    sendVisitSelection(chatId)
                } else {
                    userSurveyData[chatId]?.hobbies?.add(data)
                    sendHobbiesSelection(chatId) // Пользователь может выбрать еще одно хобби или нажать "Готово"
                }
            }
            SurveyState.ASK_VISIT -> {
                if (data == RANDOM_COFFEE_CALLBACK_MESSAGE_DONE) {
                    completeSurvey(chatId)
                } else {
                    userSurveyData[chatId]?.visit?.add(data)
                    sendVisitSelection(chatId) // Пользователь может выбрать еще одно место или нажать "Готово"
                }
            }
            else -> {
                sendMessage(chatId, RANDOM_COFFEE_HANDLING_CALLBACK_QUERY_ERROR)
            }
        }
    }

    companion object {
        val userStates = mutableMapOf<Long, SurveyState>()
        val userSurveyData = mutableMapOf<Long, SurveyData>()
        private const val START_PAGE_MENU_ID = 1
        private const val BOT_USERNAME: String = "botUsername"
        private const val BOT_TOKEN: String = "botToken"
        private const val START_MESSAGE: String = "/start"
        private const val REMINDER_MESSAGE_YES = "reminder_yes"
        private const val REMINDER_MESSAGE_ALL = "reminder_all"
        private const val REMINDER_MESSAGE_DELETE = "reminder_delete"
        private const val MESSAGE_ALL = "all"
        private const val REMINDER_MESSAGE_DELIMITER = "_event_id:"
        private const val CALLBACK_DATA_MENU_ID_3 ="menu_id:3"
        private const val CALLBACK_DATA_MENU_ID_6 ="menu_id:6"
        private const val CALLBACK_DATA_MENU_ID_7 ="menu_id:7"
        private const val CALLBACK_DATA_MENU_ID_9 ="menu_id:9"
        private const val CALLBACK_DATA_MENU_ID_10 ="menu_id:10"
        private const val CALLBACK_DATA_MENU_ID_11 ="menu_id:11"
        private const val CALLBACK_DATA_MENU_ID_13 ="menu_id:13"
        private const val ACTION_TYPE_URL = "url"
        private const val ACTION_TYPE_CALLBACK = "callback"
        private const val REMIND_MESSAGE_STATUS_SUCCESS = "Напоминание установлено!"
        private const val REMIND_MESSAGE_STATUS_DELETE = "Напоминание удалено!"
        private const val REMIND_MESSAGE_STATUS_ERROR =
            "Упс! Что-то пошло не так, свяжитесь пожалуйста с организатором!"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_NAME = "Введите ваше имя"
        private const val RANDOM_COFFEE_CALLBACK_MESSAGE_DONE = "done"
        private const val RANDOM_COFFEE_CALLBACK_MESSAGE_DONE_RUSSIA = "Готово"
        private const val RANDOM_COFFEE_SAVING_FORM_SUCCESS = "Спасибо! Ваша анкета сохранена."
        private const val RANDOM_COFFEE_INPUT_MESSAGE_AGE = "Ваш возраст:"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_OCCUPATION = "Сфера деятельности:"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_HOBBY = "Хобби: (выберите одно или несколько и нажмите 'Готово')"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_WOULD_LIKE_TO_VISIT =
            "Хочу посетить: (выберите одно или несколько и нажмите 'Готово')"
        private const val RANDOM_COFFEE_HANDLING_CALLBACK_QUERY_ERROR = "Something went wrong!"

    }
}