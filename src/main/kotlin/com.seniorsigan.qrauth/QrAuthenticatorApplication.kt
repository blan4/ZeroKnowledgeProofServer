package com.seniorsigan.qrauth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class QrAuthenticatorApplication {
    companion object {
        @JvmStatic public fun main(args: Array<String>) {
            SpringApplication.run(QrAuthenticatorApplication::class.java, *args)
        }
    }
}