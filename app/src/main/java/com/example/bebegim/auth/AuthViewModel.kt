package com.example.bebegim.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bebegim.room.DAO.UsersDao
import com.example.bebegim.room.Users
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AuthViewModel(private val usersDao: UsersDao) : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
    var isLoadingLogin by mutableStateOf(false)
    var isLoadingRegister by mutableStateOf(false)
    var isLoadingLogout by mutableStateOf(false)
    var hasInitialized by mutableStateOf(true) // Room için true
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun setCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    suspend fun isEmailRegistered(email: String): Boolean {
        return try {
            val user = usersDao.getUserByEmail(email)
            user != null
        } catch (e: Exception) {
            errorMessage = "Kullanılmayan bir email giriniz: ${e.localizedMessage}"
            false
        }
    }

    suspend fun isEmailNotRegistered(email: String): Boolean {
        return try {
            val user = usersDao.getUserByEmail(email)
            user == null
        } catch (e: Exception) {
            errorMessage = "Bu emaile ait hesap bulunamadı, lütfen hesap oluşturunuz: ${e.localizedMessage}"
            false
        }
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
                val user = usersDao.getUserByEmail(email)
                if (user != null && user.password == password) {
                    setCredentials(email, password)
                    onSuccess()
                } else {
                    onError("Giriş başarısız. Email veya şifre hatalı.")
                }
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.localizedMessage}"
            } finally {
                isLoadingLogin = false
            }
        }
    }

    fun signUpNewUser(
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoadingRegister = true
            try {
                val existingUser = usersDao.getUserByEmail(email)
                if (existingUser != null) {
                    onError("Bu email ile zaten bir kullanıcı var.")
                } else {
                    val user = Users(
                        userId = UUID.randomUUID().toString(),
                        email = email,
                        password = password,
                        fullName = fullName,
                        createdAt = getCurrentTime(),
                        updatedAt = null
                    )
                    usersDao.insertUser(user)
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Sign up failed: ${e.localizedMessage}"
            } finally {
                isLoadingRegister = false
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    fun isAdmin(): Boolean {
        return email.contains("admin", ignoreCase = true)
    }

    suspend fun logout() {
        isLoadingLogout = true
        try {
            setCredentials("", "")
        } finally {
            isLoadingLogout = false
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
