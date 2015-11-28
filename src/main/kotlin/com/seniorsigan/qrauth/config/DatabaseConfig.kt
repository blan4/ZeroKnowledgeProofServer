package com.seniorsigan.qrauth.config

import org.apache.ibatis.session.SqlSessionFactory
import org.flywaydb.core.Flyway
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@MapperScan(basePackages = arrayOf("com.seniorsigan.qrauth.core.mappers"))
open class DatabaseConfig {
    @Autowired
    lateinit public var dataSource: DataSource

    @Bean(initMethod = "migrate")
    open fun flyway(): Flyway {
        val flyway = Flyway()
        flyway.dataSource = dataSource
        return flyway
    }

    @Bean
    open fun sqlSessionFactory(): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)
        return bean.`object`
    }
}
