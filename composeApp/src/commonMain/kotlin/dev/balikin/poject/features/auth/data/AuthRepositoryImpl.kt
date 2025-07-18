package dev.balikin.poject.features.auth.data

import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import dev.balikin.poject.network.RequestHandler
import dev.balikin.poject.storage.SessionHandler
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val sessionHandler: SessionHandler,
    private val requestHandler: RequestHandler
) : AuthRepository {
    override suspend fun loginWithGoogle(): NetworkResult<LoginApiModel, NetworkException> {
        return requestHandler.get(listOf("api", "v1", "auth", "google"))
    }

    override suspend fun userInfo(): UserData {
        return UserData(
                email = sessionHandler.getEmail().first(),
                name = sessionHandler.getName().first(),
                token = sessionHandler.getToken().first()
        )
    }
}