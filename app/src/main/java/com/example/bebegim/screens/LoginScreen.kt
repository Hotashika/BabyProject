package com.example.bebegim.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.LoadingButton
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite

@Composable
fun LoginScreen(
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val isLoadingLogin = authViewModel.isLoadingLogin
    val errorMessage = authViewModel.errorMessage

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
            .background(if (isDark) DarkPastelBlue else PastelBlueWhite)
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

        /*BabyIllustration(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )*/

        Spacer(modifier = Modifier.height(16.dp))

        // Modern Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("ornek@email.com") },
            leadingIcon = {
                Icon(
                    painter = painterResource(com.example.bebegim.R.drawable.envelope_24),
                    contentDescription = "Email Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )

        // Modern Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            placeholder = { Text("Şifrenizi girin") },
            leadingIcon = {
                Icon(
                    painter = painterResource(com.example.bebegim.R.drawable.lock_24),
                    contentDescription = "Password Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible }
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible)
                                com.example.bebegim.R.drawable.eye_24
                            else
                                com.example.bebegim.R.drawable.eye_crossed_24
                        ),
                        contentDescription = if (isPasswordVisible) "Şifreyi Gizle" else "Şifreyi Göster",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        /*TextButton(
            onClick = {

            },
            Modifier.wrapContentWidth()
        ) {
            Text("Şifrenizi mi Unuttunuz?")
        }*/

        TextButton(
            onClick = {
                onNavigateToSignUp()
            },
            Modifier.wrapContentWidth()
        ) {
            Text("Hesabınız Yok Mu? Kayıt Olun")
        }
    }
}