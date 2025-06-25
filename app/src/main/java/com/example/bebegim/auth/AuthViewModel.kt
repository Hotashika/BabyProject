package com.example.bebegim.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.rangeTo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)

    var isLoadingLogin by mutableStateOf(false)
    var isLoadingRegister by mutableStateOf(false)
    var isLoadingLogout by mutableStateOf(false)

    var hasInitialized by mutableStateOf(false)
        private set

    val supabase by lazy {
        createSupabaseClient(
            supabaseUrl = "https://enpfpawkbjphocvcgbcn.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVucGZwYXdrYmpwaG9jdmNnYmNuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTA0MDY5MjksImV4cCI6MjA2NTk4MjkyOX0.0fa1OLzX9vQGajwD8njxK7lhuayRoMr5de1tSXTuAuY"
        ) {
            install(Auth)
        }
    }

    init {
        viewModelScope.launch {
            supabase.auth.awaitInitialization()
            hasInitialized = true
        }
    }

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun setCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoadingLogin = true
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.localizedMessage}"
            } finally {
                isLoadingLogin = false
            }
        }
    }

    fun signUpNewUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoadingRegister = true
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Sign up failed: ${e.localizedMessage}"
            } finally {
                isLoadingRegister = false
            }
        }
    }

    suspend fun logout() {
        isLoadingLogout = true
        try {
            supabase.auth.signOut()
            supabase.auth.signOut(SignOutScope.GLOBAL)
        } finally {
            isLoadingLogout = false
        }
    }

    fun isUserLoggedIn(): Boolean {
        return supabase.auth.currentUserOrNull() != null
    }

    fun checkLoginAndRun(onLoginSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            val currentUser = supabase.auth.currentUserOrNull()
            if (currentUser != null) {
                val isAdmin = currentUser.email?.contains("admin") ?: false
                onLoginSuccess(isAdmin)
            }
        }
    }


}
