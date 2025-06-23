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
import androidx.compose.material.icons.filled.Check


@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
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
                onCalendarAndNotesClick = { onNavigateToCalendarAndNotes },
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
                onNavigateToChatbot = onNavigateToChatbot
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
    var isEditing by remember { mutableStateOf(false) }
    var babyName by remember { mutableStateOf("Mehmet Yılmaz") }
    var babyAge by remember { mutableStateOf("3 ay") }
    var babyBirthDate by remember { mutableStateOf("10 Şubat 2023") }
    var babyGender by remember { mutableStateOf("Erkek") }
    var babyWeight by remember { mutableStateOf("5.2 kg") }
    var babyHeight by remember { mutableStateOf("58 cm") }
    var babyBloodType by remember { mutableStateOf("A+") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Bebek Bilgileri",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            if (isEditing) {
                IconButton(onClick = { isEditing = false }) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Kaydet"
                    )
                }
            }
        }
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
                    if (isEditing) {
                        OutlinedTextField(
                            value = babyName,
                            onValueChange = { babyName = it },
                            label = { Text("Bebek Adı") },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Text(
                            text = babyName,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    IconButton(onClick = { isEditing = !isEditing }) {
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
                    if (isEditing) {
                        OutlinedTextField(
                            value = babyAge,
                            onValueChange = { babyAge = it },
                            label = { Text("Yaş") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = babyBirthDate,
                            onValueChange = { babyBirthDate = it },
                            label = { Text("Doğum Tarihi") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Cinsiyet")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = babyGender == "Erkek",
                                    onClick = { babyGender = "Erkek" }
                                )
                                Text("Erkek")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = babyGender == "Kız",
                                    onClick = { babyGender = "Kız" }
                                )
                                Text("Kız")
                            }
                        }
                    } else {
                        InfoItem(label = "Yaş", value = babyAge)
                        InfoItem(label = "Doğum Tarihi", value = babyBirthDate)
                        InfoItem(label = "Cinsiyet", value = babyGender)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = babyWeight,
                            onValueChange = { babyWeight = it },
                            label = { Text("Kilo") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = babyHeight,
                            onValueChange = { babyHeight = it },
                            label = { Text("Boy") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = babyBloodType,
                            onValueChange = { babyBloodType = it },
                            label = { Text("Kan Grubu") },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        InfoItem(label = "Kilo", value = babyWeight)
                        InfoItem(label = "Boy", value = babyHeight)
                        InfoItem(label = "Kan Grubu", value = babyBloodType)
                    }
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
    onNavigateToChatbot: () -> Unit
) {
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDevices by remember { mutableStateOf(false) }

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
            onClick = { }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.lock_24),
            title = "Gizlilik & Güvenlik",
            subtitle = "Veri paylaşımı ve izinleri yönet",
            onClick = { }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.baby_18),
            title = "Bağlı Cihazlar",
            subtitle = "Cihazları görüntüle ve ekle",
            onClick = { showDevices = !showDevices }
        )
        if (showDevices) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Button(
                    onClick = { /* Cihaz 1 detaylarına gitme işlemi */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Cihaz 1", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Yeni cihaz ekleme işlemi */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text("Yeni Cihaz Ekle +", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
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
            onNavigateToChatbot = onNavigateToChatbot
        )
    }
}
@Composable
fun HelpMenu(
    onDismiss: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    var showEmailInfo by remember { mutableStateOf(false) }

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
                        onClick = { showEmailInfo = true },
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

    if (showEmailInfo) {
        AlertDialog(
            onDismissRequest = { showEmailInfo = false },
            confirmButton = {
                TextButton(onClick = { showEmailInfo = false }) {
                    Text("Tamam")
                }
            },
            title = { Text("E-posta Desteği") },
            text = {
                Text(
                    "Teknik sorunlarınız için e-posta gönderin. 24 saat içinde yanıtlanır.\n\neternaltakimi@gmail.com"
                )
            }
        )
    }
}