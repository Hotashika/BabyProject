package com.example.bebegim.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the data class for user details
data class UserDetails(
    val userId: String,
    val fullName: String,
    val email: String,
    val isAdmin: Boolean = false
)


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        // Define keys for storing preferences
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val FULL_NAME = stringPreferencesKey("full_name")
        private val EMAIL = stringPreferencesKey("email")
        private val IS_ADMIN = booleanPreferencesKey("is_admin")
    }

    // Save user login state and details
    suspend fun saveUserDetails(userDetails: UserDetails) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userDetails.userId
            preferences[FULL_NAME] = userDetails.fullName
            preferences[EMAIL] = userDetails.email
            preferences[IS_ADMIN] = userDetails.isAdmin
        }
    }

    // Get user login state as a Flow
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Get full user details as a Flow
    val userDetailsFlow: Flow<UserDetails?> = context.dataStore.data.map { preferences ->
        val userId = preferences[USER_ID]
        val fullName = preferences[FULL_NAME]
        val email = preferences[EMAIL]
        val isAdmin = preferences[IS_ADMIN] ?: false

        if (userId != null && fullName != null && email != null) {
            UserDetails(userId, fullName, email, isAdmin)
        } else {
            null
        }
    }

    // Get email directly
    val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[EMAIL] ?: ""
    }

    // Get user ID directly
    val userId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: ""
    }

    // Check if user is admin
    val isAdmin: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_ADMIN] ?: false
    }

    // Clear user data on logout
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USER_ID)
            preferences.remove(FULL_NAME)
            preferences.remove(EMAIL)
            preferences.remove(IS_ADMIN)
        }
    }
}