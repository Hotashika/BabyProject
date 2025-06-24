package com.example.bebegim.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.bebegim.R
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.BottomNavBar
import com.example.bebegim.ui.components.SettingsItem
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    authViewModel: AuthViewModel
) {

    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
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
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ProfileHeader()
            Spacer(modifier = Modifier.height(16.dp))
            BabyInformation()
            Spacer(modifier = Modifier.height(16.dp))
            SettingsSection(
                authViewModel = authViewModel,
                onNavigateBack = onNavigateBack,
                onLogout = onLogout,
                onNavigateToChatbot = onNavigateToChatbot
            )
            // Bottom padding to ensure content is not hidden behind bottom navigation
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileHeader() {
    var showPhotoDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) selectedImageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { /* Kamera ile çekilen fotoğrafı göstermek için ek işlem gerekir */ }

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
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Profil Resmi",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profil Resmi",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            FloatingActionButton(
                onClick = { showPhotoDialog = true },
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

    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Profil Fotoğrafı Seç") },
            text = {
                Column {
                    Button(
                        onClick = {
                            showPhotoDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Galeriden Fotoğraf Seç") }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showPhotoDialog = false
                            cameraLauncher.launch(null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Kamerayı Aç") }
                }
            },
            confirmButton = {}
        )
    }
}

fun calculateBabyAge(birthDateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val birthDate = LocalDate.parse(birthDateString, formatter)
    val today = LocalDate.now()
    val period = Period.between(birthDate, today)
    return "${period.years} yıl, ${period.months} ay, ${period.days} gün"
}

@Composable
fun BabyInformation() {
    var isEditing by remember { mutableStateOf(false) }
    var babyName by remember { mutableStateOf("Mehmet Yılmaz") }
    var babyBirthDate by remember { mutableStateOf("2022-08-15") }
    var babyGender by remember { mutableStateOf("Erkek") }
    var babyWeight by remember { mutableStateOf("5.2 kg") }
    var babyHeight by remember { mutableStateOf("58 cm") }
    var babyBloodType by remember { mutableStateOf("A+") }

    val yas = try {
        calculateBabyAge(babyBirthDate)
    } catch (e: Exception) {
        "-"
    }

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
                            value = babyBirthDate,
                            onValueChange = { babyBirthDate = it },
                            label = { Text("Doğum Tarihi (yyyy-MM-dd)") },
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
                        InfoItem(label = "Yaş", value = yas)
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
    val coroutineScope = rememberCoroutineScope()

    var showHelpDialog by remember { mutableStateOf(false) }
    var showDevices by remember { mutableStateOf(false) }
    var showPermissions by remember { mutableStateOf(false) }

    var notificationPermission by remember { mutableStateOf(false) }
    var audioPermission by remember { mutableStateOf(false) }
    var cameraPermission by remember { mutableStateOf(false) }

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
            onClick = { showPermissions = !showPermissions }
        )
        if (showPermissions) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bildirim İzni", modifier = Modifier.weight(1f))
                    Switch(
                        checked = notificationPermission,
                        onCheckedChange = { notificationPermission = it }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ses İzni", modifier = Modifier.weight(1f))
                    Switch(
                        checked = audioPermission,
                        onCheckedChange = { audioPermission = it }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kamera İzni", modifier = Modifier.weight(1f))
                    Switch(
                        checked = cameraPermission,
                        onCheckedChange = { cameraPermission = it }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        showPermissions = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Kaydet"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kaydet")
                }
            }
        }
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
                    Text(
                        "Yeni Cihaz Ekle +",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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

    if (showHelpDialog) {
        HelpMenu(
            onDismiss = { showHelpDialog = false },
            onNavigateToChatbot = onNavigateToChatbot
        )
    }
}

@Composable
fun PermissionDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        },
        title = { Text("İzinleri Yönet") },
        text = {
            Column {
                Button(
                    onClick = {
                        launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (permissionGranted) "Bildirim İzni Verildi" else "Bildirim İzni Al")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Ses izni iste */ },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Ses İzni Al") }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Kamera izni iste */ },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Kamera İzni Al") }
            }
        }
    )
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

@Composable
fun NotificationPermissionRequest() {
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted = isGranted
    }

    Button(onClick = {
        launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }) {
        Text(if (permissionGranted) "İzin Verildi" else "Bildirim İzni Al")
    }
}