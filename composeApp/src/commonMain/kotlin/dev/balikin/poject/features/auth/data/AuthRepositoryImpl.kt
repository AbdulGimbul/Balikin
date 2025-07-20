package dev.balikin.poject.features.auth.data

import dev.balikin.poject.features.auth.domain.GetUsersApiModel
import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import dev.balikin.poject.network.RequestHandler
import dev.balikin.poject.storage.SessionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class AuthRepositoryImpl(
    private val sessionHandler: SessionHandler,
    private val requestHandler: RequestHandler
) : AuthRepository {
    override suspend fun loginWithGoogle(): NetworkResult<LoginApiModel, NetworkException> {
        return requestHandler.get(listOf("api", "v1", "auth", "google"))
    }

    override fun userInfo(): Flow<UserData?> {
        return combine(
            sessionHandler.getEmail(),
            sessionHandler.getName(),
            sessionHandler.getToken()
        ) { email, name, token ->
            if (token.isNotEmpty()) {
                UserData(email = email, name = name, token = token)
            } else {
                null
            }
        }
    }

    override suspend fun isTokenValid(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<GetUsersApiModel, NetworkException> {
        val result = requestHandler.get<GetUsersApiModel>(
            urlPathSegments = listOf("/api/v1/list-users"),
            queryParams = mapOf(
                "keyword" to keyword,
                "limit" to limit,
                "offset" to offset
            )
        )

        if (result is NetworkResult.Error) {
            sessionHandler.clearData()
        }

        return result
    }

    override suspend fun logout() {
        sessionHandler.clearData()
    }
}