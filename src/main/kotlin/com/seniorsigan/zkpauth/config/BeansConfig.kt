package com.seniorsigan.zkpauth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.zxing.MultiFormatWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BeansConfig {
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
}
