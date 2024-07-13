package by.sf.bot.configuration

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import org.springframework.boot.jdbc.DataSourceBuilder

@Configuration
class JooqConfig {

    @Bean
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .url("jdbc:postgresql://45.135.234.61:15432/speed_friending")
            .username("username")
            .password("password")
            .build()
    }

    @Bean
    fun dsl(dataSource: DataSource): DSLContext {
        return DSL.using(dataSource, SQLDialect.POSTGRES)
    }
}