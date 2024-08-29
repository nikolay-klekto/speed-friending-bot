package by.sf.bot.component

import by.sf.bot.jooq.tables.pojos.*
import by.sf.bot.models.FullUserDataModel
import by.sf.bot.models.FullUserMatches
import by.sf.bot.models.SurveyData
import by.sf.bot.models.SurveyState
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import by.sf.bot.repository.blocking.UserBlockingRepository
import by.sf.bot.repository.impl.*
import by.sf.bot.service.AsyncMatchingService
import by.sf.bot.service.MatchingService
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.io.File
import java.time.LocalDate


class TelegramBot(
    private val mainBotInfoRepository: MainBotInfoRepository,
    private val menuInfoBlockingRepository: MenuInfoBlockingRepository,
    private val buttonRepository: ButtonRepository,
    private val userBlockingRepository: UserBlockingRepository,
    private val randomCoffeeRepository: RandomCoffeeRepository,
    private val randomCoffeeVariantsRepository: RandomCoffeeVariantsRepository,
    private val matchRepository: MatchRepository,
    private val matchingService: MatchingService,
    private val asyncMatchingService: AsyncMatchingService
) : TelegramLongPollingBot() {

    private var botUsername: String = ""
    private var botToken: String = ""
    private var menuWithButtonsCollection: HashMap<Int, HashMap<Int, Buttons>> = hashMapOf()
    private var menuInfoList: List<MenuInfo> = listOf()
    private var allAgeOptions = listOf<Ages>()
    private var allVisitOptions = listOf<PlacesToVisit>()
    private var allOccupationsOptions = listOf<Occupations>()
    private var allHobbiesOptions = listOf<Hobbies>()
    private lateinit var aboutProjectImage: InputFile
    private lateinit var ourAimsAndMissionImage: InputFile
    private lateinit var wantToSignUpImage: InputFile
    private lateinit var howEventsWorksImage: InputFile
    private lateinit var nearestEventsImage: InputFile
    private lateinit var capybaraMatchImage: InputFile
    private lateinit var setNotificationImage: InputFile
    private lateinit var purposeIdeaImage: InputFile
    private var allPhotos: HashMap<String, InputFile> = hashMapOf()

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

        val allUserMatches = matchRepository.getAllUserMatches()

        allUserMatches.forEach { currentUser ->
            val userId = currentUser.userId
            var compatibleUsers: MutableList<Int> = mutableListOf()
            var viewedUsers: MutableList<Int> = mutableListOf()

            if (!currentUser.compatibleUsers.isNullOrEmpty()) {
                try {
                    compatibleUsers =
                        currentUser.compatibleUsers?.split(",")?.map { it.toInt() }?.toMutableList() ?: mutableListOf()
                    viewedUsers =
                        currentUser.viewedUsers?.split(",")?.map { it.toInt() }?.toMutableList() ?: mutableListOf()
                } catch (e: NumberFormatException) {
                    viewedUsers = mutableListOf()
                }

            }
            userMatchesMap[userId!!] = FullUserMatches(compatibleUsers, viewedUsers)
        }

        allAgeOptions = randomCoffeeVariantsRepository.getAllAgeVariants()
        allVisitOptions = randomCoffeeVariantsRepository.getAllPlacesVariants()
        allOccupationsOptions = randomCoffeeVariantsRepository.getAllOccupationsVariants()
        allHobbiesOptions = randomCoffeeVariantsRepository.getAllHobbyVariants()

        aboutProjectImage = loadImage("photos/aboutProjectImage.jpg")
        wantToSignUpImage = loadImage("photos/wantToSignUpImage.jpg")
        howEventsWorksImage = loadImage("photos/howEventsWorksImage.jpg")
        nearestEventsImage = loadImage("photos/nearestEventsImage.jpg")
        capybaraMatchImage = loadImage("photos/capybaraMatchImage.jpg")
        setNotificationImage = loadImage("photos/setNotificationImage.jpg")
        purposeIdeaImage = loadImage("photos/purposeIdeaImage.jpg")
        ourAimsAndMissionImage = loadImage("photos/ourAimsAndMissionImage.jpg")

        allPhotos = hashMapOf(
            "aboutProjectImage" to loadImage("photos/aboutProjectImage.jpg"),
            "wantToSignUpImage" to loadImage("photos/wantToSignUpImage.jpg"),
            "howEventsWorksImage" to loadImage("photos/howEventsWorksImage.jpg"),
            "nearestEventsImage" to loadImage("photos/nearestEventsImage.jpg"),
            "capybaraMatchImage" to loadImage("photos/capybaraMatchImage.jpg"),
            "setNotificationImage" to loadImage("photos/setNotificationImage.jpg"),
            "purposeIdeaImage" to loadImage("photos/purposeIdeaImage.jpg"),
            "ourAimsAndMissionImage" to loadImage("photos/ourAimsAndMissionImage.jpg")
        )

    }

    private fun loadImage(resourcePath: String): InputFile {
        val resourceStream = this::class.java.classLoader.getResourceAsStream(resourcePath)
            ?: throw IllegalArgumentException("Image not found: $resourcePath")

        val tempFile = File.createTempFile("temp", ".jpg")
        resourceStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return InputFile(tempFile)
    }

    override fun getBotUsername(): String = botUsername
    override fun getBotToken(): String = botToken

    override fun onUpdateReceived(update: Update) {
        if (update.hasCallbackQuery()) {
            val callbackQuery = update.callbackQuery
            val callbackData = callbackQuery.data
            val callbackChatId = callbackQuery.message.chatId

            answerCallbackQuery(callbackQuery.id)


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
                CALLBACK_DATA_SHOW_MATCHES, CALLBACK_DATA_NEXT_MATCH -> showNextMatch(callbackChatId)
                CALLBACK_DATA_DELETE_PROFILE -> deleteMatchProfile(callbackChatId)

                else -> {
                    if (callbackData.startsWith(REMINDER_MESSAGE_YES)) {
                        val reminders = callbackData.split(REMINDER_MESSAGE_DELIMITER)[1]
                        sendReminderOptions(callbackChatId, reminders)
                    } else {
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

    private fun deleteMatchProfile(callbackChatId: Long?) {
        val text: String
        val idNote = randomCoffeeRepository.getIdNoteByChatId(callbackChatId!!)
        if (idNote == null) {
            text = RANDOM_COFFEE_DELETE_PROFILE_ERROR_MESSAGE
        } else {
            text = RANDOM_COFFEE_DELETE_PROFILE_MESSAGE
            randomCoffeeRepository.deleteBlocking(idNote)
            // Получаем userId пользователя по callbackChatId
            val userId = userBlockingRepository.getUserIdByChatId(callbackChatId)
            userId?.let {
                // Удаляем пользователя из userMatchesMap
                userMatchesMap.remove(it)
            }
        }
        val message = SendMessage(callbackChatId.toString(), text)
        execute(message)
    }

    private fun answerCallbackQuery(callbackQueryId: String) {
        val answer = AnswerCallbackQuery()
        answer.callbackQueryId = callbackQueryId
        execute(answer)
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

        val maxCaptionLength = 1024

        fun splitTextIntoParts(text: String, maxLength: Int): List<String> {
            if (text.length <= maxLength) return listOf(text)

            // Ищем последний знак конца предложения в пределах maxLength
            val breakpoint = text.substring(0, maxLength)
                .lastIndexOfAny(listOf(".", "!", "?"))

            // Если не найдено знаков окончания предложения, разделяем текст по максимальной длине
            val splitPoint = if (breakpoint != -1) breakpoint + 1 else maxLength

            val firstPart = text.substring(0, splitPoint).trim()
            val secondPart = text.substring(splitPoint).trim()

            return listOf(firstPart, secondPart)
        }

        val captionParts = splitTextIntoParts(text, maxCaptionLength)

        // Проверяем наличие фото для отправки
        val possiblePhotoName: String? = currentMenuModel.photoName
        if (!possiblePhotoName.isNullOrEmpty() && allPhotos.containsKey(possiblePhotoName)) {
            val imageMessage = SendPhoto()
            imageMessage.chatId = chatId.toString()
            imageMessage.caption = captionParts[0]
            imageMessage.photo = allPhotos[possiblePhotoName]!!

            // Устанавливаем разметку клавиатуры только если она есть
            if (keyboard.isNotEmpty()) {
                imageMessage.replyMarkup = inlineKeyboardMarkup
            }

            // Отправляем первое сообщение с фото
            execute(imageMessage)

            // Если есть вторая часть текста, отправляем её как отдельное сообщение
            if (captionParts.size > 1) {
                val textMessage = SendMessage()
                textMessage.chatId = chatId.toString()
                textMessage.text = captionParts[1]

                // Отправляем второе сообщение
                execute(textMessage)
            }
        } else {
            // Если фото нет, отправляем текстовое сообщение
            captionParts.forEach { part ->
                val textMessage = SendMessage()
                textMessage.chatId = chatId.toString()
                textMessage.text = part

                // Устанавливаем разметку клавиатуры только если она есть
                if (keyboard.isNotEmpty()) {
                    textMessage.replyMarkup = inlineKeyboardMarkup
                }

                // Отправляем текстовое сообщение
                execute(textMessage)
            }
        }
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
            val userId = userBlockingRepository.save(newUser)
            if (userId != null) {
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
                userStates[chatId] = SurveyState.ASK_TELEGRAM_USERNAME
                sendMessage(chatId, RANDOM_COFFEE_INPUT_MESSAGE_TELEGRAM_USERNAME)
            }

            SurveyState.ASK_TELEGRAM_USERNAME -> {
                userSurveyData[chatId]?.telegramUsername = response
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun completeSurvey(chatId: Long) {

        sendMessage(chatId, RANDOM_COFFEE_SAVING_FORM_SUCCESS)

        val surveyData = userSurveyData[chatId]

        userStates.remove(chatId)

        var userId: Int? = userBlockingRepository.getUserIdByChatId(chatId)

        if (userId == null) {
            userId = userBlockingRepository.save(
                Users(
                    telegramId = chatId
                )
            )
        }

        val newRandomCoffee = RandomCoffee(
            userId = userId,
            username = surveyData?.name,
            telegramUsername = surveyData?.telegramUsername
        )

        val newRandomCoffeeIdNote: Int
        if (!randomCoffeeRepository.isRandomCoffeeModelExist(userId!!)) {
            newRandomCoffeeIdNote = randomCoffeeRepository.saveBlock(newRandomCoffee).idNote!!
        } else {
            newRandomCoffeeIdNote = randomCoffeeRepository.updateBlock(newRandomCoffee)
            randomCoffeeVariantsRepository.deleteAllVariantsByRandomCoffeeId(newRandomCoffeeIdNote)

        }


        // Сохранение возраста
        val ageId = allAgeOptions.filter{it.ageRange == surveyData?.age}.map { it.ageId }.first()
        ageId?.let {
            randomCoffeeVariantsRepository.saveCoffeeAge(
                RandomCoffeeAge(
                    randomCoffeeId = newRandomCoffeeIdNote,
                    ageId = it
                )
            )
        }

        // Сохранение сферы деятельности
        val occupationId = allOccupationsOptions.filter{it.occupation == surveyData?.occupation}.map { it.occupationId }.first()
        occupationId?.let {
            randomCoffeeVariantsRepository.saveCoffeeOccupation(
                RandomCoffeeOccupation(
                    randomCoffeeId = newRandomCoffeeIdNote,
                    occupationId = it
                )
            )
        }

        // Сохранение хобби и мест для посещения через массовые вставки
        val hobbies = surveyData?.hobbies?.mapNotNull { hobby ->
            val hobbyId = allHobbiesOptions.filter{it.hobby == hobby}.map { it.hobbyId }.first()
            hobbyId?.let { RandomCoffeeHobby(randomCoffeeId = newRandomCoffeeIdNote, hobbyId = it) }
        }?.toSet()

        val places = surveyData?.visit?.mapNotNull { place ->
            val placeId = allVisitOptions.filter{it.place == place}.map { it.placeId }.first()
            placeId?.let { RandomCoffeePlace(randomCoffeeId = newRandomCoffeeIdNote, placeId = it) }
        }?.toSet()

        // Используйте массовые вставки
        hobbies?.let { randomCoffeeVariantsRepository.saveAllCoffeeHobbies(it) }
        places?.let { randomCoffeeVariantsRepository.saveAllCoffeePlaces(it) }

        // Запуск фоновой задачи для пересчета вероятностей и обновления userMatchesMap
        GlobalScope.launch {
            asyncMatchingService.recalculateAllMatches()
        }
    }

    private fun sendAgeSelection(chatId: Long) {
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = RANDOM_COFFEE_INPUT_MESSAGE_AGE

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val rows: MutableList<List<InlineKeyboardButton>> = ArrayList()


        allAgeOptions.map { it.ageRange!! }.forEach { age ->
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

        allOccupationsOptions.map { it.occupation!! }.forEach { occupation ->
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

        allHobbiesOptions.map { it.hobby!! }.forEach { hobby ->
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

        allVisitOptions.map { it.place!! }.forEach { option ->
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
//                        sendHobbiesSelection(chatId) // Пользователь может выбрать еще одно хобби или нажать "Готово"
                }
            }

            SurveyState.ASK_VISIT -> {
                if (data == RANDOM_COFFEE_CALLBACK_MESSAGE_DONE) {
                    completeSurvey(chatId)
                } else {
                    userSurveyData[chatId]?.visit?.add(data)
//                        sendVisitSelection(chatId) // Пользователь может выбрать еще одно место или нажать "Готово"
                }
            }

            else -> {
                sendMessage(chatId, RANDOM_COFFEE_HANDLING_CALLBACK_QUERY_ERROR)
            }
        }
    }

    private fun sendUserSurvey(chatId: Long, user: FullUserDataModel?) {

        val hobbies = user?.hobbies?.joinToString(", ") ?: "Неизвестно"
        val visit = user?.visit?.joinToString(", ") ?: "Неизвестно"

        val messageText = """
        Имя: ${user?.name ?: "Неизвестно"}
        Возраст: ${user?.age ?: "Неизвестно"}
        Сфера деятельности: ${user?.occupation ?: "Неизвестно"}
        Хобби: $hobbies
        Хочу посетить: $visit
        Контакты: ${user?.telegramUsername ?: "Неизвестно"}
    """.trimIndent()

//        sendMessage(chatId, messageText)

        val nextButton = InlineKeyboardButton()
        nextButton.text = "Просмотреть следующую"
        nextButton.callbackData = "next_survey"

        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = listOf(listOf(nextButton))

        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = messageText
        message.replyMarkup = inlineKeyboardMarkup

        execute(message)
    }

    private fun showNextMatch(chatId: Long) {
        val userId = userBlockingRepository.getUserIdByChatId(chatId)
        val matches = userMatchesMap[userId] ?: return sendMessage(chatId, "Пожалуйста, заполните сначала анкету.")

        val remainingUsers = matches.compatibleUsers.filterNot { matches.viewedUsers.contains(it) }

        if (remainingUsers.isNotEmpty()) {
            val nextUserId = remainingUsers.first()

            val nextUser = randomCoffeeRepository.getFullUserData(nextUserId)


            // Отправка анкеты пользователю
            sendUserSurvey(chatId, nextUser)

            // Обновляем список просмотренных анкет
            matches.viewedUsers.add(nextUserId)
            userMatchesMap[userId!!] = matches
            matchingService.saveAllMatchesInDB()

        } else {
            sendMessage(chatId, "Нет больше анкет для просмотра.")
        }
    }


    companion object {
        val userStates = mutableMapOf<Long, SurveyState>()
        val userSurveyData = mutableMapOf<Long, SurveyData>()
        val userMatchesMap: MutableMap<Int, FullUserMatches> = mutableMapOf()
        private const val START_PAGE_MENU_ID = 1
        private const val BOT_USERNAME: String = "botUsername"
        private const val BOT_TOKEN: String = "botToken"
        private const val START_MESSAGE: String = "/start"
        private const val REMINDER_MESSAGE_YES = "reminder_yes"
        private const val REMINDER_MESSAGE_ALL = "reminder_all"
        private const val REMINDER_MESSAGE_DELETE = "reminder_delete"
        private const val MESSAGE_ALL = "all"
        private const val REMINDER_MESSAGE_DELIMITER = "_event_id:"
        private const val CALLBACK_DATA_MENU_ID_3 = "menu_id:3"
        private const val CALLBACK_DATA_MENU_ID_6 = "menu_id:6"
        private const val CALLBACK_DATA_MENU_ID_7 = "menu_id:7"
        private const val CALLBACK_DATA_MENU_ID_9 = "menu_id:9"
        private const val CALLBACK_DATA_MENU_ID_10 = "menu_id:10"
        private const val CALLBACK_DATA_MENU_ID_11 = "menu_id:11"
        private const val CALLBACK_DATA_MENU_ID_13 = "menu_id:13"
        private const val CALLBACK_DATA_SHOW_MATCHES = "show_matches"
        private const val CALLBACK_DATA_NEXT_MATCH = "next_survey"
        private const val CALLBACK_DATA_DELETE_PROFILE = "delete_profile"
        private const val ACTION_TYPE_URL = "url"
        private const val ACTION_TYPE_CALLBACK = "callback"
        private const val REMIND_MESSAGE_STATUS_SUCCESS = "Напоминание установлено!"
        private const val REMIND_MESSAGE_STATUS_DELETE = "Напоминание удалено!"
        private const val REMIND_MESSAGE_STATUS_ERROR =
            "Упс! Что-то пошло не так, свяжитесь пожалуйста с организатором!"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_NAME = "Введите ваше имя"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_TELEGRAM_USERNAME =
            "Введите информацию, по которой с вами можно связаться: ссылка на инстаграмм/телеграмм"
        private const val RANDOM_COFFEE_CALLBACK_MESSAGE_DONE = "done"
        private const val RANDOM_COFFEE_CALLBACK_MESSAGE_DONE_RUSSIA = "✅ Готово"
        private const val RANDOM_COFFEE_SAVING_FORM_SUCCESS = "Спасибо! Ваша анкета сохранена."
        private const val RANDOM_COFFEE_INPUT_MESSAGE_AGE = "Ваш возраст:"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_OCCUPATION = "Сфера деятельности:"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_HOBBY =
            "Хобби: (выберите одно или несколько и нажмите 'Готово')"
        private const val RANDOM_COFFEE_INPUT_MESSAGE_WOULD_LIKE_TO_VISIT =
            "Хочу посетить: (выберите одно или несколько и нажмите 'Готово')"
        private const val RANDOM_COFFEE_HANDLING_CALLBACK_QUERY_ERROR = "Something went wrong!"
        private const val RANDOM_COFFEE_DELETE_PROFILE_MESSAGE = "Анкета успешно удалена!"
        private const val RANDOM_COFFEE_DELETE_PROFILE_ERROR_MESSAGE = "Ваша анкета ещё не заполнена!"
    }
}