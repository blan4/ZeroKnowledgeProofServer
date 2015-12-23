package org.seniorsigan.zkpauth.core.services

import java.util.*

fun String.toBase64() = String(Base64.getEncoder().encode(this.toByteArray()))
fun String.fromBase64() = String(Base64.getDecoder().decode(this))
