package dev.balikin.poject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform