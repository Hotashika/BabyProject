package com.example.bebegim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bebegim.R
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.components.SettingsItem

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onLogout: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onHomeClick = onNavigateBack,
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onProfileClick = { }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader()
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            BabyInformation()
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            SettingsSection(
                authViewModel = authViewModel,
                onNavigateBack = onNavigateBack,
                onLogout = onLogout,
                onNavigateToChatbot = onNavigateToChatbot // BURAYA EKLENDİ
            )
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RectangleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Profil Resmi",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            FloatingActionButton(
                onClick = { /* TODO: Profil resmini düzenle */ },
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_24),
                    contentDescription = "Profil Resmini Düzenle",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ayşe Yılmaz",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "ayse@example.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "Ebeveyn",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun BabyInformation() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Bebek Bilgileri",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baby_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mehmet Yılmaz",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* TODO: Bebek bilgilerini düzenle */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_24),
                            contentDescription = "Bebek Bilgilerini Düzenle"
                        )
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = "Yaş", value = "3 ay")
                    InfoItem(label = "Doğum Tarihi", value = "10 Şubat 2023")
                    InfoItem(label = "Cinsiyet", value = "Erkek")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = "Kilo", value = "5.2 kg")
                    InfoItem(label = "Boy", value = "58 cm")
                    InfoItem(label = "Kan Grubu", value = "A+")
                }
            }
        }
    }
}

@Composable
fun InfoItem(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SettingsSection(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToChatbot: () -> Unit  // BURAYA EKLENDİ
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Ayarlar",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingsItem(
            icon = painterResource(id = R.drawable.bell_notification_social_media_24),
            title = "Bildirimler",
            subtitle = "Uyarılar ve hatırlatıcıları ayarla",
            onClick = { /* Bildirim ayarlarına git */ }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.lock_24),
            title = "Gizlilik & Güvenlik",
            subtitle = "Veri paylaşımı ve izinleri yönet",
            onClick = { /* Gizlilik ayarlarına git */ }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.broken_chain_link_wrong_24),
            title = "Bağlı Cihazlar",
            subtitle = "Bebek monitörleri ve sensörleri yönet",
            onClick = { /* Cihaz ayarlarına git */ }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.info_24),
            title = "Yardım & Destek",
            subtitle = "SSS ve iletişim bilgileri",
            onClick = { showHelpDialog = true }
        )
    }
    if (showHelpDialog) {
        HelpMenu(
            onDismiss = { showHelpDialog = false },
            onNavigateToChatbot = onNavigateToChatbot // BURAYA EKLENDİ
        )
    }
}

@Composable
fun HelpMenu(
    onDismiss: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Yardım & Destek") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(
                        onClick = { /* E-posta Desteği */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        )
                    ) {
                        Text("E-posta Desteği")
                    }
                    Button(
                        onClick = {
                            onNavigateToChatbot()
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        )
                    ) {
                        Text("Canlı Destek")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* Dökümanlar */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        )
                    ) {
                        Text("Dökümanlar")
                    }
                    Button(
                        onClick = { /* Sistem Durumu */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        )
                    ) {
                        Text("Sistem Durumu")
                    }
                }
            }
        }
    )
}
