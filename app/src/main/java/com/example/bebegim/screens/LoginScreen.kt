package com.example.bebegim.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.BabyIllustration
import com.example.bebegim.ui.components.LoadingButton

@Composable
fun LoginScreen(
    onLoginSuccess: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoadingLogin = authViewModel.isLoadingLogin
    val isLoadingRegister = authViewModel.isLoadingRegister
    val errorMessage = authViewModel.errorMessage

    // Oturum kontrolü
    LaunchedEffect(authViewModel.hasInitialized) {
        if (authViewModel.hasInitialized) {
            authViewModel.checkLoginAndRun(onLoginSuccess)
        }
    }

    // Hata mesajı varsa Toast göster
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            authViewModel.errorMessage = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "BabyGuard Uygulamasına\nHoş Geldiniz!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        BabyIllustration(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        LoadingButton(
            text = "Giriş Yap",
            isLoading = isLoadingLogin,
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    authViewModel.errorMessage = "Lütfen email ve şifre alanlarını doldurun."
                    return@LoadingButton
                }
                authViewModel.signInWithEmail(
                    email = email,
                    password = password,
                    onSuccess = {
                        val isAdmin = email.contains("admin")
                        onLoginSuccess(isAdmin)
                    },
                    onError = {
                        authViewModel.errorMessage = "Giriş başarısız. Email veya şifrenizi kontrol edin."
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        LoadingButton(
            text = "Kayıt Ol",
            isLoading = isLoadingRegister,
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    authViewModel.errorMessage = "Kayıt için lütfen tüm alanları doldurun."
                    return@LoadingButton
                }
                authViewModel.signUpNewUser(
                    email = email,
                    password = password,
                    onSuccess = {
                        val isAdmin = email.contains("admin")
                        onLoginSuccess(isAdmin)
                    },
                    onError = {
                        authViewModel.errorMessage = "Kayıt başarısız. Email zaten kullanılıyor olabilir veya şifre çok zayıf."
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = {
                // TODO: Navigate to ForgotPasswordScreen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Şifrenizi mi Unuttunuz?")
        }
    }
}
