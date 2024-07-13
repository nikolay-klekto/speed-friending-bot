package by.sf.bot.configuration

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

//import org.springframework.cache.CacheManager
//import org.springframework.cache.annotation.EnableCaching
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
//import org.springframework.data.redis.core.ReactiveRedisTemplate
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.RedisSerializationContext
//import org.springframework.data.redis.serializer.RedisSerializer
//
//
//@Configuration
//@EnableCaching
//class CacheConfig {
//
//    @Bean
//    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
//        val redisConfig = RedisStandaloneConfiguration("45.135.234.61", 6379)
//        redisConfig.setPassword("password")
//        val lettuceClientConfiguration = LettuceClientConfiguration.builder().build()
//
//        return LettuceConnectionFactory(redisConfig, lettuceClientConfiguration)
//    }
//
//    @Bean
//    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Any> {
//        val serializer = GenericJackson2JsonRedisSerializer()
//        val serializationContext = RedisSerializationContext
//            .newSerializationContext<String, Any>(serializer)
//            .build()
//        return ReactiveRedisTemplate(connectionFactory, serializationContext)
//    }
//}

//@Configuration
//class RedisConfig {
//    @Bean
//    fun redisConnectionFactory(): LettuceConnectionFactory {
//        val redisConfig = RedisStandaloneConfiguration()
//        redisConfig.hostName = "45.135.234.61"
//        redisConfig.port = 6379
//        redisConfig.password = RedisPassword.of("password")
//        return LettuceConnectionFactory(redisConfig)
//    }
//
//    @Bean
//    fun commandLineRunner(redisConnectionFactory: LettuceConnectionFactory): CommandLineRunner {
//        return CommandLineRunner {
//            println("Redis Host: ${redisConnectionFactory.hostName}")
//            println("Redis Port: ${redisConnectionFactory.port}")
//        }
//    }
//}