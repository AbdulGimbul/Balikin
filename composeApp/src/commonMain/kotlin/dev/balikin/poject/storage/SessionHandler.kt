package dev.balikin.poject.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class SessionHandler(private val dataStore: DataStore<Preferences>) {

    companion object {
        val _email = stringPreferencesKey("Email")
        val _name = stringPreferencesKey("Nama")
        val _userToken = stringPreferencesKey("Token")
        val _isFirstTime = booleanPreferencesKey("is_first_time")
    }

    private fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    fun getEmail(): Flow<String> = getPreference(_email, "")
    fun getName(): Flow<String> = getPreference(_name, "")
    fun getToken(): Flow<String> = getPreference(_userToken, "")
    fun isFirstTime(): Flow<Boolean> = getPreference(_isFirstTime, true)

    suspend fun getStoredToken(): String {
        return getToken().first()
    }

    suspend fun hasValidToken(): Boolean {
        val token = getStoredToken()
        return token.isNotEmpty() && !isTokenExpired(token)
    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalTime::class)
    private fun isTokenExpired(token: String): Boolean {
        return try {
            // JWT format: header.payload.signature
            val parts = token.split(".")
            if (parts.size != 3) return true

            // Decode the payload (second part)
            val payload = parts[1]
            // Add padding if needed for Base64 decoding
            val paddedPayload = payload + "=".repeat((4 - payload.length % 4) % 4)
            val decodedBytes = Base64.decode(paddedPayload)
            val payloadJson = decodedBytes.decodeToString()

            // Parse JSON to get expiration time
            val json = Json.parseToJsonElement(payloadJson).jsonObject
            val exp = json["exp"]?.jsonPrimitive?.content?.toLongOrNull()

            if (exp == null) {
                // If no expiration time, consider it expired for safety
                println("JWT: No expiration time found, considering expired")
                return true
            }

            val expirationTime = Instant.fromEpochSeconds(exp)
            val currentTime = Clock.System.now()
            val isExpired = currentTime >= expirationTime

            println("JWT: Current time: $currentTime, Expiration: $expirationTime, Expired: $isExpired")
            isExpired
        } catch (e: Exception) {
            println("JWT: Error parsing token: ${e.message}")
            // If we can't parse the token, consider it expired
            true
        }
    }

    suspend fun setUserData(
        email: String,
        nama: String,
        token: String
    ) {
        dataStore.edit { pref ->
            pref[_email] = email
            pref[_name] = nama
            pref[_userToken] = token
        }
    }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[_isFirstTime] = false
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            val currentIsFirstTime = preferences[_isFirstTime] ?: true
            preferences.clear()
            preferences[_isFirstTime] = currentIsFirstTime
        }
    }
}