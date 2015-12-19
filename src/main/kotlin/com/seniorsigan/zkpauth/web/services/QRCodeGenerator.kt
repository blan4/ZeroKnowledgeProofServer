package com.seniorsigan.zkpauth.web.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.image.BufferedImage

@Service
class QRCodeGenerator
@Autowired constructor(
    val writer: MultiFormatWriter,
    val objectMapper: ObjectMapper
) {

    fun generate(data: String): BufferedImage {
        val bitmap = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400)
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

        return image
    }

    fun generateFromObject(data: Any): BufferedImage {
        val dataStr = objectMapper.writeValueAsString(data)
        return generate(dataStr)
    }
}
