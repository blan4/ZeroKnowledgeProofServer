package org.seniorsigan.zkpauth.config

import org.springframework.context.annotation.Bean
import org.springframework.session.data.redis.config.ConfigureRedisAction
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@EnableRedisHttpSession
open class HttpSessionConfig {
    @Bean
    open fun configureRedisAction(): ConfigureRedisAction {
        return ConfigureRedisAction.NO_OP
    }
}
