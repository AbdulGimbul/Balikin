package dev.balikin.poject.features.auth.domain

data class UserData(
    val name: String,
    val email: String,
    val token: String
)