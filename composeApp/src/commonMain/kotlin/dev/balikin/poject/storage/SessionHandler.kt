package dev.balikin.poject.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

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