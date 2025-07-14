package dev.balikin.poject.features.auth.data

import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import dev.balikin.poject.network.RequestHandler

class AuthRepositoryImpl(
    private val requestHandler: RequestHandler
) : AuthRepository {
    override suspend fun loginWithGoogle(): NetworkResult<LoginApiModel, NetworkException> {
        return requestHandler.get(listOf("api", "v1", "auth", "google"))
    }
}