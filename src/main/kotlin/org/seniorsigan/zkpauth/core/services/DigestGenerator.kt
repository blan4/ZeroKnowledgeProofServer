package org.seniorsigan.zkpauth.core.services

import org.springframework.stereotype.Service
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

@Service
class DigestGenerator {
    val algorythm: String = "SHA-256"

    fun generateSequence(nonce: String, times: Int): List<String> {
        val bNonce = nonce.toByteArray("UTF-8")
        val seq: MutableList<ByteArray> = arrayListOf()
        seq.add(generate(bNonce))
        for (i in 1..times) {
            seq.add(i, generate(seq.last()))
        }

        return seq.map { DatatypeConverter.printHexBinary(it) }
    }

    fun generate(nonce: ByteArray): ByteArray {
        val md = MessageDigest.getInstance(algorythm)
        md.update(nonce)
        return md.digest()
    }

    fun generate(nonce: String): String {
        val bNonce = DatatypeConverter.parseHexBinary(nonce)
        val md = MessageDigest.getInstance(algorythm)
        md.update(bNonce)
        return DatatypeConverter.printHexBinary(md.digest())
    }
}
