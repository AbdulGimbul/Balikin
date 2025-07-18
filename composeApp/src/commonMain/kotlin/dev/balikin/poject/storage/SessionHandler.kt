package dev.balikin.poject.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}