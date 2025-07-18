package dev.balikin.poject.features.auth.data

import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult

interface AuthRepository {
    suspend fun loginWithGoogle(): NetworkResult<LoginApiModel, NetworkException>
    suspend fun userInfo(): UserData
}