package com.dorianbanic.heyapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform