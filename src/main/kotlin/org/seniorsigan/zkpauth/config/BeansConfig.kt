package org.seniorsigan.zkpauth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.zxing.MultiFormatWriter
import org.seniorsigan.zkpauth.lib.SchnorrSignature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
open class BeansConfig: WebMvcConfigurerAdapter() {
    @Bean
    open fun multiFormatWriter(): MultiFormatWriter {
        return MultiFormatWriter()
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        //some config could be there
        return mapper
    }

    @Bean
    open fun schnorrSignature(): SchnorrSignature = SchnorrSignature()
}
