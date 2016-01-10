package org.seniorsigan.zkpauth.web.controllers

import org.seniorsigan.zkpauth.core.models.SKeySecret
import org.seniorsigan.zkpauth.core.models.SKeyUser
import org.seniorsigan.zkpauth.core.repositories.LoginRequestRepository
import org.seniorsigan.zkpauth.core.repositories.SKeyUserRepository
import org.seniorsigan.zkpauth.core.repositories.SignupRequestRepository
import org.seniorsigan.zkpauth.core.repositories.UserRepository
import org.seniorsigan.zkpauth.core.services.DigestGenerator
import org.seniorsigan.zkpauth.core.services.toBase64
import org.seniorsigan.zkpauth.web.models.CommonResponse
import org.seniorsigan.zkpauth.web.models.LoginForm
import org.seniorsigan.zkpauth.web.models.SignupForm
import org.seniorsigan.zkpauth.web.services.*
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
    val userRepository: SKeyUserRepository,
    val loginRequestRepository: LoginRequestRepository,
    val sKeyTokenGenerator: SKeyTokenGenerator,
    val schonorrTokenGenerator: SchonorrTokenGenerator,
    val qrCodeGenerator: QRCodeGenerator,
    val signupRequestRepository: SignupRequestRepository
) {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(request: HttpServletRequest, model: Model): String {
        val user = request.session.getAttribute("user")
        if (user != null) {
            model.addAttribute("user", user as String)
        } else {
            val sKeyLoginToken = sKeyTokenGenerator.createLoginJson(request)
            val sKeySignupToken = sKeyTokenGenerator.createSignupJson(request)
            model.addAttribute("sKeyLoginToken", sKeyLoginToken.toBase64())
            model.addAttribute("sKeySignupToken", sKeySignupToken.toBase64())

            val schonorrLoginToken = sKeyTokenGenerator.createLoginJson(request)
            val schonorrSignupToken = sKeyTokenGenerator.createSignupJson(request)
            model.addAttribute("schonorrLoginToken", schonorrLoginToken.toBase64())
            model.addAttribute("schonorrSignupToken", schonorrSignupToken.toBase64())
        }
        return "index"
    }

    @RequestMapping(value = "/login/skey", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun sKeyLogin(@RequestBody form: LoginForm): CommonResponse {
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
                loginRequestRepository.delete(loginRequest)
                println("User $user logged in")
                return CommonResponse(true, "", "user logged in")
            }
        }

        return CommonResponse(false, "Can't find user")
    }

    @RequestMapping(value = "/login/skey.png", method = arrayOf(RequestMethod.GET))
    fun requestSKeyLogin(request: HttpServletRequest, response: ServletResponse) {
        val token = sKeyTokenGenerator.createLogin(request)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/signup/skey.png", method = arrayOf(RequestMethod.GET))
    fun requestSKeySignUp(request: HttpServletRequest, response: ServletResponse) {
        val token = sKeyTokenGenerator.createSignup(request)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/login/schonorr.png", method = arrayOf(RequestMethod.GET))
    fun requestSchonorrLogin(request: HttpServletRequest, response: ServletResponse) {
        val token = schonorrTokenGenerator.createLogin(request)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/signup/schonorr.png", method = arrayOf(RequestMethod.GET))
    fun requestSchonorrSignUp(request: HttpServletRequest, response: ServletResponse) {
        val token = schonorrTokenGenerator.createSignup(request)
        buildQRCodeResponse(token, response)
    }

    private fun buildQRCodeResponse(token: Any, response: ServletResponse) {
        val image = qrCodeGenerator.generateFromObject(token)

        response.contentType = "image/png"
        ImageIO.write(image, "png", response.outputStream)
    }

    @RequestMapping(value = "/signup/skey", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun sKeySignUp(@RequestBody form: SignupForm): CommonResponse {
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

        val user = SKeyUser(login = form.login, secret = SKeySecret(form.key))
        userRepository.save(user)
        signupRequestRepository.delete(signupRequest)
        sessionService.bound(signupRequest.sessionId, user.login)

        println("Created user $user")
        return CommonResponse(true, "", "success created user with login ${user.login}")
    }

    @RequestMapping(value = "/login/schonorr", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun schonorrLogin(@RequestBody form: LoginForm): CommonResponse {
        return CommonResponse(false, "Not supported")
    }

    @RequestMapping(value = "/signup/schonorr", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun schonorrSignup(@RequestBody form: SignupForm): CommonResponse {
        return CommonResponse(false, "Not supported")
    }
}
