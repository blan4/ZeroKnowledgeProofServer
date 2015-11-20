package com.seniorsigan.qrauth.web.controllers

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*
import javax.imageio.ImageIO
import javax.servlet.ServletResponse

@Controller
class MainController {
    
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun index(): String {
        return "Hello World!"
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