package nl.project.michaelmunatsi.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.project.michaelmunatsi.model.Token
import javax.inject.Inject
import javax.inject.Singleton

// this class contains code for storing and retrieving authentication token from preferences
private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "key_store")

@Singleton
class UserManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore: DataStore<Preferences> by lazy {
        context._dataStore
    }

    companion object {
        private val TOKEN = stringPreferencesKey("AUTH_TOKEN")
    }

    // get saved authentication token
    val getAuthToken: Flow<Token?> = dataStore.data.map { preferences ->
        preferences[TOKEN]?.let { Token(it) }
    }

    // save login authentication token
    suspend fun saveAuthToken(authToken: Token) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = authToken.AuthToken
        }
    }

    suspend fun deleteAuthToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}