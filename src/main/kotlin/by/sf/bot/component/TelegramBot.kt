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
                    sendReminderOptions(callbackChatId, "all")
                }

                REMINDER_MESSAGE_DELETE -> {
                    sendReminderOptions(callbackChatId, null)
                }

                "6" -> sendMenuInfo(callbackChatId, 6)
                "7" -> sendMenuInfo(callbackChatId, 7)
                "3" -> sendMenuInfo(callbackChatId, 3)
                "9" -> sendMenuInfo(callbackChatId, 9)
                "10" -> sendMenuInfo(callbackChatId, 10)
                "11" -> sendMenuInfo(callbackChatId, 11)
                "13" -> startSurvey(callbackChatId)

                else -> {
                    if(callbackData.startsWith("reminder_yes")){
                        val reminders = callbackData.split("_event_id:")[1]
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
            startButtons?.get(2)?.label -> sendMenuInfo(chatId, 3)  // Пример menu_id
            startButtons?.get(3)?.label -> sendMenuInfo(chatId, 4)
            startButtons?.get(4)?.label -> sendMenuInfo(chatId, 12)  // Пример menu_id
//            startButtons?.get(5)?.label -> sendReminderOptions(chatId)
//            startButtons?.get(6)?.label -> sendRandomCoffeeInfo(chatId)
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
//        row2.add(currentButtonList[5]!!.label)
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
                "url" -> {
                    if (!button.actionData.isNullOrEmpty()) {
                        inlineKeyboardButton.url = button.actionData
                        keyboard.add(listOf(inlineKeyboardButton))
                    }
                }

                "callback" -> {
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

    private fun sendParticipationForm(chatId: Long) {
//        val text = "Заполняя данную анкету вы автоматически попадаете в лист тех людей, которым мы напишем лично, чтобы пригласить/уточнить о планах.\n\nhttps://forms.gle/fizJkaNjUa6yea477"
//        sendMessage(chatId, text)
    }

    private fun sendEventDetails(chatId: Long) {
//        val text = "Долой 5 минут общения за столиком, звонок будильника и смену партнеров! Вот как проходят наши мероприятия:\n\n1. В день мероприятия мы ждем вас по указанному адресу. Видео как добраться мы пришлем, но если потеряетесь, смело пишите, заберем и поможем.\n2. В гардеробе можете оставить вещи, взять тапочки и пройти дальше на регистрацию.\n3. После регистрации, мы выдаем каждому участнику бейдж с именем, чтобы облегчить первый контакт.\n4. Ждем пару минут, рассказываем про нас и помещение и начинаем активную программу мероприятия.\n5. Активности под руководством ведущего, помогают начать общение и найти тему разговора.\n6. В середине программы вас ждет кофе-брейк, чтобы взять чашку кофе/чая и набраться сил перед продолжением программы.\n7. В конце мероприятия есть время на обмен контактами с теми, с кем вы хотели бы продолжить общение. Но если нужно будет убежать раньше, мы создаем чат со всеми участниками и поможем поддержать общение после ивента.\n8. На протяжении всего Speed Friending у нас работает фотограф, так что смело обращайтесь к ней за снимками на память и сохранением приятных моментов.\n\nКаждое мероприятие Speed Friending создано, чтобы комфортно и легко завести новые знакомства в атмосфере дружбы и поддержки.\n\nЗаписывайтесь в анкете кнопка “заполнить анкету” и “написать организатору”"
//        sendMessage(chatId, text)
    }

    private fun sendReminderOptions(chatId: Long, reminders: String?) {

        val userExistStatus = userBlockingRepository.isUserExist(chatId)

        if (userExistStatus) {

            val updateStatus = userBlockingRepository.update(chatId, reminders)

            if (updateStatus) {
                if (reminders != null) {
                    sendMessage(chatId, "Напоминание установлено!")
                } else sendMessage(chatId, "Напоминание удалено!")

            } else sendMessage(chatId, "Упс! Что-то пошло не так, свяжитесь пожалуйста с организатором!")

        } else if (reminders != null) {
            val newUser = Users(
                telegramId = chatId,
                dateCreated = LocalDate.now(),
                reminders = reminders
            )
            val saveStatus = userBlockingRepository.save(newUser)
            if (saveStatus) {
                sendMessage(chatId, "Напоминание установлено!")
            } else sendMessage(chatId, "Упс! Что-то пошло не так, свяжитесь пожалуйста с организатором!")
        } else sendMessage(chatId, "Success!")
//        sendMessage(chatId, text)
    }

    private fun sendRandomCoffeeInfo(chatId: Long) {
        val text = "Success!"

        sendMessage(chatId, text)
    }

    private fun startSurvey(chatId: Long) {
        userStates[chatId] = SurveyState.ASK_NAME
        userSurveyData[chatId] = SurveyData()

        sendMessage(chatId, "Введите ваше имя:")
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
                if (response == "Готово") {
                    userStates[chatId] = SurveyState.ASK_VISIT
                    sendVisitSelection(chatId)
                } else {
                    userSurveyData[chatId]?.hobbies?.add(response)
                    sendHobbiesSelection(chatId)
                }
            }

            SurveyState.ASK_VISIT -> {
                if (response == "done") {
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

        sendMessage(chatId, "Спасибо! Ваша анкета сохранена.")
    }

    private fun sendAgeSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = "Ваш возраст:"

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
        message.text = "Сфера деятельности:"

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
        message.text = "Хобби: (выберите одно или несколько и нажмите 'Готово')"

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
        doneButton.text = "Готово"
        doneButton.callbackData = "done"
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
        message.text = "Хочу посетить: (выберите одно или несколько и нажмите 'Готово')"

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
        doneButton.text = "Готово"
        doneButton.callbackData = "done"
        val doneRow: MutableList<InlineKeyboardButton> = ArrayList()
        doneRow.add(doneButton)
        rows.add(doneRow)

        inlineKeyboardMarkup.keyboard = rows
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun sendVisitOptions(chatId: Long) {
        val options = listOf("Музей", "Кино", "Рок-фестиваль")
        sendOptions(chatId, "Хочу посетить:", options)
    }

    private fun sendOptions(chatId: Long, text: String, options: List<String>) {
        val keyboardMarkup = ReplyKeyboardMarkup()
        val keyboard: MutableList<KeyboardRow> = ArrayList()
        options.forEach {
            val row = KeyboardRow()
            row.add(it)
            keyboard.add(row)
        }
        keyboardMarkup.keyboard = keyboard
        keyboardMarkup.resizeKeyboard = true
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        message.replyMarkup = keyboardMarkup
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
                if (data == "done") {
                    userStates[chatId] = SurveyState.ASK_VISIT
                    sendVisitSelection(chatId)
                } else {
                    userSurveyData[chatId]?.hobbies?.add(data)
                    sendHobbiesSelection(chatId) // Пользователь может выбрать еще одно хобби или нажать "Готово"
                }
            }
            SurveyState.ASK_VISIT -> {
                if (data == "done") {
                    completeSurvey(chatId)
                } else {
                    userSurveyData[chatId]?.visit?.add(data)
                    sendVisitSelection(chatId) // Пользователь может выбрать еще одно место или нажать "Готово"
                }
            }
            else -> {
                sendMessage(chatId, "Something went wrong!")
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
    }
}