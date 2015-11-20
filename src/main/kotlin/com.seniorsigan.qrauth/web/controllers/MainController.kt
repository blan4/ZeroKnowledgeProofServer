package com.seniorsigan.qrauth.web.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MainController {
    
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun index(): String {
        return "Hello World!"
    }
}