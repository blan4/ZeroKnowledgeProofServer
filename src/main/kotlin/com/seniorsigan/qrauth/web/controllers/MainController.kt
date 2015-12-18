package com.seniorsigan.qrauth.web.controllers

import com.seniorsigan.qrauth.core.DigestGenerator
import com.seniorsigan.qrauth.core.models.User
import com.seniorsigan.qrauth.core.repositories.LoginRequestRepository
import com.seniorsigan.qrauth.core.repositories.SignupRequestRepository
import com.seniorsigan.qrauth.core.repositories.UserRepository
import com.seniorsigan.qrauth.web.models.CommonResponse
import com.seniorsigan.qrauth.web.models.SignInForm
import com.seniorsigan.qrauth.web.models.SignupForm
import com.seniorsigan.qrauth.web.services.QRCodeGenerator
import com.seniorsigan.qrauth.web.services.SessionService
import com.seniorsigan.qrauth.web.services.TokenGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Controller
class MainController
@Autowired constructor(
    val service: DigestGenerator,
    val sessionService: SessionService,
    val userRepository: UserRepository,
    val loginRequestRepository: LoginRequestRepository,
    val tokenGenerator: TokenGenerator,
    val qrCodeGenerator: QRCodeGenerator,
    val signupRequestRepository: SignupRequestRepository
) {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(request: HttpServletRequest, model: Model): String {
        val user = request.session.getAttribute("user")
        if (user != null) {
            model.addAttribute("user", user as String)
        }
        return "index"
    }

    @RequestMapping(value = "/login", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun login(@RequestBody form: SignInForm): CommonResponse {
        println("Get login form $form")
        if (form.key.isBlank() || form.login.isBlank() || form.token.isBlank()) {
            return CommonResponse(false, "invalid login form")
        }
        val loginRequest = loginRequestRepository.findByToken(form.token) ?: return CommonResponse(false, "Can't find login request by token")
        if (loginRequest.expiresAt <= Date()) {
            loginRequestRepository.delete(loginRequest)
            return CommonResponse(false, "Login request expired")
        }
        val user = userRepository.find(form.login)
        if (user != null) {
            val nextKey = service.generate(form.key)
            if (nextKey == user.token) {
                user.token = form.key
                userRepository.update(user)
                sessionService.bound(loginRequest.sessionId, user.login)
                println("User $user logged in")
                return CommonResponse(true, "", "user logged in")
            }
        }

        return CommonResponse(false, "Can't find user")
    }

    @RequestMapping(value = "/login.png", method = arrayOf(RequestMethod.GET))
    fun requestLogin(request: HttpServletRequest, response: ServletResponse) {
        val token = tokenGenerator.createLogin(request)
        val image = qrCodeGenerator.generateFromObject(token)

        response.contentType = "image/png"
        ImageIO.write(image, "png", response.outputStream)
    }

    @RequestMapping(value = "/signup.png", method = arrayOf(RequestMethod.GET))
    fun requestSignUp(request: HttpServletRequest, response: ServletResponse) {
        val token = tokenGenerator.createSignup(request)
        val image = qrCodeGenerator.generateFromObject(token)

        response.contentType = "image/png"
        ImageIO.write(image, "png", response.outputStream)
    }

    @RequestMapping(value = "/signup", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun signUp(@RequestBody form: SignupForm): CommonResponse {
        println("Get signup form $form")
        if (form.key.isBlank() || form.login.isBlank() || form.token.isBlank()) {
            return CommonResponse(false, "invalid signup form")
        }

        if (userRepository.find(form.login) != null) {
            return CommonResponse(false, "user with login ${form.login} already exists")
        }

        val signupRequest = signupRequestRepository.findByToken(form.token) ?: return CommonResponse(false, "Can't find signup request by token")

        if (signupRequest.expiresAt <= Date()) {
            signupRequestRepository.delete(signupRequest)
            return CommonResponse(false, "Signup request expired")
        }

        val user = User(login = form.login, token = form.key)
        userRepository.save(user)
        sessionService.bound(signupRequest.sessionId, user.login)

        println("Created user $user")
        return CommonResponse(true, "", "success created user with login ${user.login}")
    }
}
