package dev.balikin.poject.features.auth.data

import dev.balikin.poject.features.auth.domain.GetUsersApiModel
import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithGoogle(): NetworkResult<LoginApiModel, NetworkException>
    fun userInfo(): Flow<UserData?>
    suspend fun isTokenValid(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<GetUsersApiModel, NetworkException>
    suspend fun logout(): Result<Unit>
}