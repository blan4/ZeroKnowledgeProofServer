package org.seniorsigan.zkpauth.web.controllers

import org.seniorsigan.zkpauth.core.models.RequestInfo
import org.seniorsigan.zkpauth.core.models.SKeySecret
import org.seniorsigan.zkpauth.core.models.SKeyUser
import org.seniorsigan.zkpauth.core.models.SchnorrUser
import org.seniorsigan.zkpauth.core.repositories.SKeyUserRepository
import org.seniorsigan.zkpauth.core.repositories.SchnorrUserRepository
import org.seniorsigan.zkpauth.core.services.*
import org.seniorsigan.zkpauth.web.models.CommonResponse
import org.seniorsigan.zkpauth.web.models.LoginForm
import org.seniorsigan.zkpauth.web.models.SchonorrSignupForm
import org.seniorsigan.zkpauth.web.models.SignupForm
import org.seniorsigan.zkpauth.web.services.QRCodeGenerator
import org.seniorsigan.zkpauth.web.services.SKeyTokenGenerator
import org.seniorsigan.zkpauth.web.services.SchnorrTokenGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class MainController
@Autowired constructor(
    val service: DigestGenerator,
    val sessionService: SessionService,
    val sKeyUserRepository: SKeyUserRepository,
    val schnorrUserRepository: SchnorrUserRepository,
    val sKeyTokenGenerator: SKeyTokenGenerator,
    val schnorrTokenGenerator: SchnorrTokenGenerator,
    val qrCodeGenerator: QRCodeGenerator,
    val schnorrService: SchnorrService,
    val sessionTokenService: SessionTokenService
) {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(request: HttpServletRequest, model: Model): String {
        val user = request.session.getAttribute("user")
        if (user != null) {
            model.addAttribute("user", user as String)
        } else {
            val token = sessionTokenService.createToken(request.session.id, requestInfo(request))

            val sKeyLoginToken = sKeyTokenGenerator.createLoginJson(token)
            val sKeySignupToken = sKeyTokenGenerator.createSignupJson(token)
            model.addAttribute("sKeyLoginToken", sKeyLoginToken.toBase64())
            model.addAttribute("sKeySignupToken", sKeySignupToken.toBase64())

            val schnorrLoginToken = schnorrTokenGenerator.createLoginJson(token)
            val schnorrSignupToken = schnorrTokenGenerator.createSignupJson(token)
            model.addAttribute("schnorrLoginToken", schnorrLoginToken.toBase64())
            model.addAttribute("schnorrSignupToken", schnorrSignupToken.toBase64())
        }
        return "index"
    }

    @RequestMapping(value = "/checkLogin", method = arrayOf(RequestMethod.HEAD))
    fun checkLogin(request: HttpServletRequest, response: HttpServletResponse) {
        val user = request.session.getAttribute("user")
        response.status = if (user != null) {
            HttpStatus.OK.value()
        } else {
            HttpStatus.UNAUTHORIZED.value()
        }
    }

    @RequestMapping(value = "/login/skey", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun sKeyLogin(@RequestBody form: LoginForm): CommonResponse {
        println("Get skey login form $form")
        if (form.key.isBlank() || form.login.isBlank() || form.token.isBlank()) {
            return CommonResponse(false, "invalid login form")
        }

        try {
            val sessionId = sessionTokenService.use(form.token)
            val user = sKeyUserRepository.find(form.login)
            if (user != null) {
                val nextKey = service.generate(form.key)
                if (nextKey == user.token) {
                    user.token = form.key
                    user.tokensUsed++
                    sKeyUserRepository.update(user)
                    sessionService.bound(sessionId, user.login)
                    println("User $user logged in")
                    return CommonResponse(true, "", "user logged in")
                }
            }
        } catch(e: ServiceException) {
            return CommonResponse(false, e.message ?: "Can't use token ${form.token}")
        }

        return CommonResponse(false, "Can't find user")
    }

    @RequestMapping(value = "/login/skey.png", method = arrayOf(RequestMethod.GET))
    fun requestSKeyLogin(request: HttpServletRequest, response: HttpServletResponse) {
        val st = sessionTokenService.createToken(request.session.id, requestInfo(request))
        val token = sKeyTokenGenerator.createLogin(st)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/signup/skey.png", method = arrayOf(RequestMethod.GET))
    fun requestSKeySignUp(request: HttpServletRequest, response: HttpServletResponse) {
        val st = sessionTokenService.createToken(request.session.id, requestInfo(request))
        val token = sKeyTokenGenerator.createSignup(st)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/login/schnorr.png", method = arrayOf(RequestMethod.GET))
    fun requestSchonorrLogin(request: HttpServletRequest, response: HttpServletResponse) {
        val st = sessionTokenService.createToken(request.session.id, requestInfo(request))
        val token = schnorrTokenGenerator.createLogin(st)
        buildQRCodeResponse(token, response)
    }

    @RequestMapping(value = "/signup/schnorr.png", method = arrayOf(RequestMethod.GET))
    fun requestSchonorrSignUp(request: HttpServletRequest, response: HttpServletResponse) {
        val st = sessionTokenService.createToken(request.session.id, requestInfo(request))
        val token = schnorrTokenGenerator.createSignup(st)
        buildQRCodeResponse(token, response)
    }

    private fun buildQRCodeResponse(token: Any, response: HttpServletResponse) {
        val image = qrCodeGenerator.generateFromObject(token)
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.contentType = "image/png"
        ImageIO.write(image, "png", response.outputStream)
    }

    @RequestMapping(value = "/signup/skey", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun sKeySignUp(@RequestBody form: SignupForm): CommonResponse {
        println("Get skey signup form $form")
        if (form.key.isBlank() || form.login.isBlank() || form.token.isBlank()) {
            return CommonResponse(false, "invalid signup form")
        }

        if (sKeyUserRepository.find(form.login) != null) {
            return CommonResponse(false, "user with login ${form.login} already exists")
        }

        try {
            val sessionId = sessionTokenService.use(form.token)
            val user = SKeyUser(login = form.login, secret = SKeySecret(form.key))
            sKeyUserRepository.save(user)
            sessionService.bound(sessionId, user.login)
            println("Created user $user with algorithm ${user.algorithm}")
            return CommonResponse(true, "", "successfully created user with login ${user.login} and algorithm ${user.algorithm}")
        } catch(e: ServiceException) {
            return CommonResponse(false, e.message ?: "Can't use token ${form.token}")
        }
    }

    @RequestMapping(value = "/login/schnorr", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun schnorrLogin(@RequestBody form: LoginForm): CommonResponse {
        println("Get schnorr login form $form")
        if (form.key.isBlank() || form.login.isBlank() || form.token.isBlank()) {
            println("Invalid login form $form")
            return CommonResponse(false, "Invalid login form $form")
        }
        val user = schnorrUserRepository.find(form.login)
        if (user == null) {
            println("Can't find user ${form.login}")
            return CommonResponse(false, "Can't find user ${form.login}")
        }

        try {
            val message = schnorrService.invoke(user, form.token, form.key)
            return CommonResponse(success = true, message = message)
        } catch(e: ServiceException) {
            println(e.message)
            return CommonResponse(false, e.message ?: "Can't use token ${form.token}")
        }
    }

    @RequestMapping(value = "/signup/schnorr", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun schnorrSignup(@RequestBody form: SchonorrSignupForm): CommonResponse {
        println("Get schnorr signup form $form")
        if (!form.valid()) {
            return CommonResponse(false, "invalid signup form")
        }
        if (!schnorrService.isValidPublicKey(form.key)) {
            return CommonResponse(false, "invalid schnorr public key form")
        }

        if (schnorrUserRepository.find(form.login) != null) {
            return CommonResponse(false, "user with login ${form.login} already exists")
        }

        try {
            val sessionId = sessionTokenService.use(form.token)
            val user = SchnorrUser(login = form.login, secret = form.key)
            schnorrUserRepository.save(user)
            sessionService.bound(sessionId, user.login)
            println("Created user $user with algorithm ${user.algorithm}")
            return CommonResponse(true, "", "successfully created user with login ${user.login} and algorithm ${user.algorithm}")
        } catch(e: ServiceException) {
            return CommonResponse(false, e.message ?: "Can't use token ${form.token}")
        }
    }

    @RequestMapping(value = "/logout", method = arrayOf(RequestMethod.POST))
    fun logout(request: HttpServletRequest): String {
        request.session.invalidate()
        return "redirect:/"
    }
    
    private fun requestInfo(request: HttpServletRequest): RequestInfo {
        return RequestInfo(
                ip = request.remoteAddr,
                host = request.remoteHost,
                port = request.remotePort,
                userAgent = request.getHeader("User-Agent"))
    }
}
