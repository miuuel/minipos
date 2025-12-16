package com.miguelveliz.minipos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform