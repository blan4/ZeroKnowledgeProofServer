package com.seniorsigan.qrauth.web.controllers

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.seniorsigan.qrauth.core.DigestGenerator
import com.seniorsigan.qrauth.web.models.SignUpForm
import com.seniorsigan.qrauth.web.services.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Controller
class MainController
@Autowired constructor(
    val service: DigestGenerator,
    val sessionService: SessionService
) {
    val users: MutableMap<String, String> = hashMapOf()


    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(request: HttpServletRequest, model: Model): String {
        val user = request.session.getAttribute("user")
        if (user != null) {
            model.addAttribute("user", user as String)
        }
        sessionService.show(request.session.id)
        return "index"
    }

    @RequestMapping(value = "/login", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun login(@ModelAttribute form: SignUpForm, request: HttpServletRequest): String {
        if (form.key.isBlank() || form.login.isBlank()) {
            return "invalid login form"
        }
        val key = users[form.login]
        if (key != null) {
            val nextKey = service.generate(form.key)
            if (key == nextKey) {
                users[form.login] = form.key
                request.session.setAttribute("user", form.login)
                return "logged in"
            }
        }
        return "fail"
    }

    @RequestMapping(value = "/signup", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun signUp(@ModelAttribute form: SignUpForm): String {
        if (form.key.isBlank() || form.login.isBlank()) {
            return "invalid signUp form"
        }

        if (users[form.login] == null) {
            users[form.login] = form.key
            return "success"
        }

        return "fail: user already exists"
    }

    @RequestMapping(value = "/generateCode", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun generateQRCode(response: ServletResponse) {
        val uuid = UUID.randomUUID().toString()
        val writer = MultiFormatWriter()
        val bitmap = writer.encode(uuid, BarcodeFormat.QR_CODE, 400, 400)
        val image = BufferedImage(bitmap.width, bitmap.height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.graphics
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, bitmap.width, bitmap.height)
        graphics.color = Color.BLACK

        for (x in 0..bitmap.width - 1) {
            for (y in 0..bitmap.height - 1) {
                if (bitmap.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1)
                }
            }
        }

        response.contentType = "image/png"
        ImageIO.write(image, "png", response.outputStream)
    }
}
