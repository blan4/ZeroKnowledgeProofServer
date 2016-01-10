package org.seniorsigan.zkpauth.core.services

class ServiceException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)
