package by.sf.bot.component

import by.sf.bot.jooq.tables.pojos.Buttons
import by.sf.bot.jooq.tables.pojos.MenuInfo
import by.sf.bot.repository.blocking.MenuInfoBlockingRepository
import by.sf.bot.repository.impl.ButtonRepository
import by.sf.bot.repository.impl.MainBotInfoRepository
import by.sf.bot.repository.impl.MenuInfoRepository
import jakarta.annotation.PostConstruct
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


class TelegramBot(
    private val mainBotInfoRepository: MainBotInfoRepository,
    private val menuInfoBlockingRepository: MenuInfoBlockingRepository,
    private val buttonRepository: ButtonRepository,
    private val menuInfoRepository: MenuInfoRepository,
    private val dsl: DSLContext
) : TelegramLongPollingBot() {

    private var botUsername: String = ""
    private var botToken: String = ""
    private var menuWithButtonsCollection: HashMap<String, HashMap<Int, Buttons>> = hashMapOf()
    private var menuInfoList: List<MenuInfo> = listOf()

    @PostConstruct
    fun init() {
        botUsername = mainBotInfoRepository.getMainBotInfoByKey(BOT_USERNAME).block()?.value!!
        botToken = mainBotInfoRepository.getMainBotInfoByKey(BOT_TOKEN).block()?.value!!
        menuInfoList = menuInfoBlockingRepository.getAllMenuModels()

        val listMenuTitles: List<String> = menuInfoBlockingRepository.getAllMenuTitles()

        listMenuTitles.forEach {menuTitle->
            val currentButtons: List<Buttons> = buttonRepository.getAllButtonsByMenuTitle(menuTitle)
            currentButtons.forEach { currentButton->
                if (menuWithButtonsCollection.containsKey(menuTitle)) {
                    // Вставляем данные во вложенную карту
                    menuWithButtonsCollection[menuTitle]?.put(currentButton.position!!, currentButton)
                } else {
                    // Создаем новую вложенную карту и вставляем данные
                    val innerMap = HashMap<Int, Buttons>()
                    innerMap[currentButton.position!!] = currentButton
                    menuWithButtonsCollection[menuTitle] = innerMap
                }
            }

        }

        // Другие данные, которые нужно загрузить
    }

    fun loadDataFromDatabase(){

    }

    override fun getBotUsername(): String = botUsername
    override fun getBotToken(): String = botToken

    override fun onUpdateReceived(update: Update) {
        val message = update.message
        val chatId = message.chatId

        val startButtons = menuWithButtonsCollection["start"]

        when (message.text) {
            startButtons?.get(1)?.label-> sendStartMessage(chatId)
            startButtons?.get(2)?.label -> sendMenuInfo(chatId, 1)  // Пример menu_id
            startButtons?.get(3)?.label -> sendParticipationForm(chatId)
            startButtons?.get(4)?.label -> sendMenuInfo(chatId, 2)  // Пример menu_id
            startButtons?.get(5)?.label -> sendReminderOptions(chatId)
            startButtons?.get(6)?.label -> sendRandomCoffeeInfo(chatId)
            else -> sendMessage(chatId, "Неизвестная команда. Попробуйте снова.")
        }
    }

    private fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage(chatId.toString(), text)
        execute(message)
    }

    private fun sendStartMessage(chatId: Long) {
        val currentMenuModel = menuInfoList.find { it.parentId == null }
        val currentButtonList = menuWithButtonsCollection[currentMenuModel?.title]

        val text = currentMenuModel!!.description!!
        val keyboardMarkup = ReplyKeyboardMarkup()
        val keyboard: MutableList<KeyboardRow> = ArrayList()
        val row = KeyboardRow()
        row.add(currentButtonList?.get(1)!!.label)
        row.add(currentButtonList[2]!!.label)
        row.add(currentButtonList[3]!!.label)
        row.add(currentButtonList[4]!!.label)
        row.add(currentButtonList[5]!!.label)
        keyboard.add(row)
        keyboardMarkup.keyboard = keyboard
        keyboardMarkup.resizeKeyboard = true
        val message = SendMessage()
        message.chatId = chatId.toString()
        message.text = text
        message.replyMarkup = keyboardMarkup
        execute(message)
    }

    private fun sendMenuInfo(chatId: Long, menuId: Int) {
//        menuInfoService.getMenuInfo(menuId).flatMap { menuInfo ->
//            buttonService.getButtonsByMenuId(menuId).collectList().flatMap { buttons ->
//                val text = menuInfo?.description ?: "Информация не найдена"
//                sendMessage(chatId, text)
                // Добавьте код для отправки кнопок на основе данных из buttons
//                Mono.empty<Void>()
//            }
//        }.subscribe()
    }

    private fun sendParticipationForm(chatId: Long) {
//        val text = "Заполняя данную анкету вы автоматически попадаете в лист тех людей, которым мы напишем лично, чтобы пригласить/уточнить о планах.\n\nhttps://forms.gle/fizJkaNjUa6yea477"
//        sendMessage(chatId, text)
    }

    private fun sendEventDetails(chatId: Long) {
//        val text = "Долой 5 минут общения за столиком, звонок будильника и смену партнеров! Вот как проходят наши мероприятия:\n\n1. В день мероприятия мы ждем вас по указанному адресу. Видео как добраться мы пришлем, но если потеряетесь, смело пишите, заберем и поможем.\n2. В гардеробе можете оставить вещи, взять тапочки и пройти дальше на регистрацию.\n3. После регистрации, мы выдаем каждому участнику бейдж с именем, чтобы облегчить первый контакт.\n4. Ждем пару минут, рассказываем про нас и помещение и начинаем активную программу мероприятия.\n5. Активности под руководством ведущего, помогают начать общение и найти тему разговора.\n6. В середине программы вас ждет кофе-брейк, чтобы взять чашку кофе/чая и набраться сил перед продолжением программы.\n7. В конце мероприятия есть время на обмен контактами с теми, с кем вы хотели бы продолжить общение. Но если нужно будет убежать раньше, мы создаем чат со всеми участниками и поможем поддержать общение после ивента.\n8. На протяжении всего Speed Friending у нас работает фотограф, так что смело обращайтесь к ней за снимками на память и сохранением приятных моментов.\n\nКаждое мероприятие Speed Friending создано, чтобы комфортно и легко завести новые знакомства в атмосфере дружбы и поддержки.\n\nЗаписывайтесь в анкете кнопка “заполнить анкету” и “написать организатору”"
//        sendMessage(chatId, text)
    }

    private fun sendReminderOptions(chatId: Long) {
//        val text = "Нужно ли вам напомнить о встрече за 3 дня до мероприятия?"
//        sendMessage(chatId, text)
        // Добавьте кнопки "Да" и "Нет"
    }

    private fun sendRandomCoffeeInfo(chatId: Long) {
//        val text = "Привет! Добро пожаловать в Random Coffee. Готовы найти друга по интересам?"
//        sendMessage(chatId, text)
        // Добавьте кнопки "Да" и "Нет"
    }

    companion object{
        private const val BOT_USERNAME: String = "botUsername"
        private const val BOT_TOKEN: String = "botToken"
    }
}