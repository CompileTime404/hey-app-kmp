package com.dorianbanic.core.domain.logging

interface HeyappLogger {
    fun info(message: String)
    fun debug(message: String)
    fun warn(message: String)
    fun error(message: String, throwable: Throwable? = null)
}