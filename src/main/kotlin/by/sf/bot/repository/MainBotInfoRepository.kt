//package by.sf.bot.repository
//
//import by.sf.bot.models.MainBotInfoModel
//import org.springframework.data.repository.reactive.ReactiveCrudRepository
//import org.springframework.stereotype.Repository
//import reactor.core.publisher.Mono
//
//@Repository
//interface MainBotInfoRepository : ReactiveCrudRepository<MainBotInfoModel, Int>{
//    fun findByKey(key: String): Mono<MainBotInfoModel>
//
//}