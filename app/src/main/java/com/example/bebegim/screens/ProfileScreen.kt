package com.example.bebegim.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bebegim.R
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.components.SettingsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
    onLogout: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val errorMessage = authViewModel.errorMessage

    Scaffold(

        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onHomeClick = onNavigateBack,
                onChatClick = onNavigateToChatbot,
                onReportsClick = onNavigateToReports,
                onCalendarAndNotesClick = onNavigateToCalendarAndNotes,
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
                onLogout = onLogout
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
                    InfoItem(
                        label = "Yaş",
                        value = "3 ay"
                    )

                    InfoItem(
                        label = "Doğum Tarihi",
                        value = "10 Şubat 2023"
                    )

                    InfoItem(
                        label = "Cinsiyet",
                        value = "Erkek"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(
                        label = "Kilo",
                        value = "5.2 kg"
                    )

                    InfoItem(
                        label = "Boy",
                        value = "58 cm"
                    )

                    InfoItem(
                        label = "Kan Grubu",
                        value = "A+"
                    )
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
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

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
            icon = painterResource(id = R.drawable.lock_24),
            title = "Gizlilik & Güvenlik",
            subtitle = "Veri paylaşımı ve izinleri yönet",
            onClick = { /* TODO: Gizlilik ayarlarına git */ }
        )

        SettingsItem(
            icon = painterResource(id = R.drawable.broken_chain_link_wrong_24),
            title = "Bağlı Cihazlar",
            subtitle = "Bebek monitörleri ve sensörleri yönet",
            onClick = { /* TODO: Cihaz ayarlarına git */ }
        )

        SettingsItem(
            icon = painterResource(id = R.drawable.info_24),
            title = "Yardım & Destek",
            subtitle = "SSS ve iletişim bilgileri",
            onClick = { /* TODO: Yardım ve destek sayfasına git */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        val isLoadingLogout = authViewModel.isLoadingLogout

        Button(
            onClick = {
                coroutineScope.launch {
                    authViewModel.logout()
                    onLogout()
                }
            },
            enabled = !isLoadingLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            if (isLoadingLogout) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onError,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.exit_24),
                    contentDescription = "Çıkış ikonu"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Çıkış Yap")
            }
        }


        Spacer(modifier = Modifier.height(24.dp))
    }
}
