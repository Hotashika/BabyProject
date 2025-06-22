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
fun SignupScreen(
    onSignupSuccess: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoadingRegister = authViewModel.isLoadingRegister
    val errorMessage = authViewModel.errorMessage

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

        Spacer(modifier = Modifier.height(60.dp))

        SignupTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Tam Adınız",
            iconRes = com.example.bebegim.R.drawable.user_24,
            keyboardType = KeyboardType.Text
        )

        SignupTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "ornek@email.com",
            iconRes = com.example.bebegim.R.drawable.envelope_24,
            keyboardType = KeyboardType.Email
        )

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Şifre",
            placeholder = "En az 6 karakter",
            iconRes = com.example.bebegim.R.drawable.lock_24,
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }
        )

        LoadingButton(
            text = "Kayıt Ol",
            isLoading = isLoadingRegister,
            onClick = {
                if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                    authViewModel.errorMessage = "Lütfen tüm alanları doldurun."
                    return@LoadingButton
                }

                authViewModel.signUpNewUser(
                    email = email,
                    password = password,
                    onSuccess = {
                        val isAdmin = email.contains("admin")
                        onSignupSuccess(isAdmin)
                    },
                    onError = {
                        authViewModel.errorMessage = "Kayıt başarısız. Email zaten kullanılıyor olabilir veya şifre çok zayıf."
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SignupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    iconRes: Int,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) ({ Text(placeholder) }) else null,
        leadingIcon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "$label icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
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
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    iconRes: Int,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) ({ Text(placeholder) }) else null,
        leadingIcon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "$label icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible)
                            com.example.bebegim.R.drawable.eye_crossed_24
                        else
                            com.example.bebegim.R.drawable.eye_24
                    ),
                    contentDescription = if (passwordVisible) "Şifreyi Gizle" else "Şifreyi Göster",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
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
}