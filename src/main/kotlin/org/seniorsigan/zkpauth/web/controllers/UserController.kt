package org.seniorsigan.zkpauth.web.controllers

import org.seniorsigan.zkpauth.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
open class UserController
@Autowired constructor(
    val userRepository: UserRepository
) {
    @RequestMapping(value = "/users", method = arrayOf(RequestMethod.GET))
    fun getAllUsers(model: Model): String {
        val users = userRepository.findAll()
        model.addAttribute("users", users)
        return "users"
    }
}
