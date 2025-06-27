package com.example.bebegim.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.theme.Poppins
import com.example.bebegim.R
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AuthScreen(
    onNavigateToLogin: () -> Unit,
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onNavigateToSignup: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val isLoadingLogin = authViewModel.isLoadingLogin
    val errorMessage = authViewModel.errorMessage

    // Animasyon değişkenleri
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.3f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleScale = remember { Animatable(0.8f) }
    val buttonsAlpha = remember { Animatable(0f) }
    val buttonsTranslationY = remember { Animatable(50f) }
    val copyrightAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo animasyonu
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }

        delay(300)

        // Başlık animasyonu
        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }
        launch {
            titleScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }

        delay(400)

        // Butonlar animasyonu
        launch {
            buttonsAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }
        launch {
            buttonsTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 600)
            )
        }

        delay(200)

        // Copyright animasyonu
        copyrightAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
    }

    LaunchedEffect(authViewModel.hasInitialized) {
        if (authViewModel.hasInitialized) {
            authViewModel.checkLoginAndRun(onLoginSuccess)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkPastelBlue else PastelBlueWhite)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo veya ikon alanı
        Icon(
            painter = painterResource(id = R.drawable.babyguard_profile_photo_v2),
            contentDescription = "BabyGuard Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(logoAlpha.value)
                .scale(logoScale.value),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "BabyGuard",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .alpha(titleAlpha.value)
                .scale(titleScale.value)
        )

        Spacer(modifier = Modifier.height(2.dp))

        /*Text(
            text = "Bebeğiniz için güvenli bir dünya",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )*/

        Spacer(modifier = Modifier.height(180.dp))

        Column(
            modifier = Modifier
                .alpha(buttonsAlpha.value)
                .padding(top = buttonsTranslationY.value.dp)
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Giriş Yap",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onNavigateToSignup,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Kayıt Ol",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Eternal © 2025",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(copyrightAlpha.value)
        )
    }
}