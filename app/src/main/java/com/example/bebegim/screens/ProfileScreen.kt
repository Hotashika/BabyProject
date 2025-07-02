package com.example.bebegim.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bebegim.R
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.room.AppDatabase
import com.example.bebegim.ui.components.SettingsItem
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins
import com.example.bebegim.viewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.text.get

data class BabyInfo(
    val name: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val weight: String = "",
    val height: String = "",
    val bloodType: String = ""
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToCalendarAndNotes: () -> Unit,
    onNavigateToChatbot: () -> Unit,
    authViewModel: AuthViewModel,
    email: String
) {
    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val usersDao = db.UsersDao()
    val babiesDao = db.BabiesDao()

    // Use ProfileViewModel
    val profileViewModel: ProfileViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(usersDao) as T
        }
    })
    LaunchedEffect(email) {
        profileViewModel.loadUserByEmail(email)
    }
    val user = profileViewModel.user.value

    val userId = user?.userId
    val babies by if (userId != null) {
        babiesDao.getBabiesByUserId(userId).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList<com.example.bebegim.room.Babies>()) }
    }
    val baby = babies.firstOrNull()

    Scaffold(
        containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkPastelBlue else PastelBlueWhite,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ProfileHeader(email = email, fullName = user?.fullName ?: "-", isLoading = user == null)
            Spacer(modifier = Modifier.height(16.dp))
            BabyInformationDisplay(
                babyInfo = baby?.let {
                    BabyInfo(
                        name = it.name ?: "",
                        birthDate = it.birthDate ?: "",
                        gender = it.gender ?: "",
                        weight = it.currentWeight?.toString() ?: "",
                        height = it.currentHeight?.toString() ?: "",
                        bloodType = it.bloodType ?: ""
                    )
                } ?: BabyInfo(),
                babyId = baby?.babyId
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsSection(
                authViewModel = authViewModel,
                onNavigateBack = onNavigateBack,
                onLogout = onLogout,
                onNavigateToChatbot = onNavigateToChatbot
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileHeader(email: String, fullName: String, isLoading: Boolean = false) {
    val context = LocalContext.current
    val db = remember {
        AppDatabase.getInstance(context)
    }
    val usersDao = db.UsersDao()


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

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = fullName,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            text = email,
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
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Galeriden Fotoğraf Seç") }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            cameraLauncher.launch(null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Kamerayı Aç") }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPhotoDialog = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Kaydet"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kaydet")
                }
            }
        )
    }
}

fun calculateBabyAge(birthDateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val birthDate = LocalDate.parse(birthDateString, formatter)
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)
        period.years.toString()
    } catch (e: Exception) {
        "-1"
    }
}



@Composable
fun BabyInformationDisplay(babyInfo: BabyInfo, babyId: String? = null) {
    var isEditing by remember { mutableStateOf(false) }
    var editableBabyInfo by remember { mutableStateOf(babyInfo) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val db = remember {
        AppDatabase.getInstance(context)
    }
    val babiesDao = db.BabiesDao()

    val scope = rememberCoroutineScope()

    // Date picker dialog for editing
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            editableBabyInfo = editableBabyInfo.copy(
                birthDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            )
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

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
                IconButton(onClick = {
                    isEditing = false
                    // Save changes if needed
                    scope.launch {
                        if (babyId != null) {
                            babiesDao.updateBabyById(babyId, editableBabyInfo.name, editableBabyInfo.birthDate)
                        }
                    }
                }) {
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
                            value = editableBabyInfo.name,
                            onValueChange = { editableBabyInfo = editableBabyInfo.copy(name = it) },
                            label = { Text("Bebek Adı") },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Text(
                            text = babyInfo.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    IconButton(onClick = {
                        if (isEditing) {
                            editableBabyInfo = babyInfo // Reset changes
                        }
                        isEditing = !isEditing
                    }) {
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
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = editableBabyInfo.birthDate,
                                onValueChange = { },
                                label = { Text("Doğum Tarihi") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { datePickerDialog.show() }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Tarih Seç"
                                        )
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Cinsiyet")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = editableBabyInfo.gender == "Erkek",
                                    onClick = { editableBabyInfo = editableBabyInfo.copy(gender = "Erkek") }
                                )
                                Text("Erkek")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = editableBabyInfo.gender == "Kız",
                                    onClick = { editableBabyInfo = editableBabyInfo.copy(gender = "Kız") }
                                )
                                Text("Kız")
                            }
                        }
                    } else {
                        val age = calculateBabyAge(babyInfo.birthDate)
                        InfoItem(label = "Yaş", value = age)
                        InfoItem(label = "Doğum Tarihi", value = babyInfo.birthDate)
                        InfoItem(label = "Cinsiyet", value = babyInfo.gender)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editableBabyInfo.weight,
                            onValueChange = { editableBabyInfo = editableBabyInfo.copy(weight = it) },
                            label = { Text("Kilo") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = editableBabyInfo.height,
                            onValueChange = { editableBabyInfo = editableBabyInfo.copy(height = it) },
                            label = { Text("Boy") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = editableBabyInfo.bloodType,
                            onValueChange = { editableBabyInfo = editableBabyInfo.copy(bloodType = it) },
                            label = { Text("Kan Grubu") },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        InfoItem(label = "Kilo", value = babyInfo.weight)
                        InfoItem(label = "Boy", value = babyInfo.height)
                        InfoItem(label = "Kan Grubu", value = babyInfo.bloodType)
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
    var showDevicesDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    var notificationPermission by remember { mutableStateOf(false) }
    var audioPermission by remember { mutableStateOf(false) }
    var cameraPermission by remember { mutableStateOf(false) }

    var tempNotificationPermission by remember { mutableStateOf(notificationPermission) }
    var tempAudioPermission by remember { mutableStateOf(audioPermission) }
    var tempCameraPermission by remember { mutableStateOf(cameraPermission) }

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
            onClick = {
                tempNotificationPermission = notificationPermission
                tempAudioPermission = audioPermission
                tempCameraPermission = cameraPermission
                showPrivacyDialog = true
            }
        )
        SettingsItem(
            icon = painterResource(id = R.drawable.baby_18),
            title = "Bağlı Cihazlar",
            subtitle = "Cihazları görüntüle ve ekle",
            onClick = { showDevicesDialog = true }
        )
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

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        notificationPermission = tempNotificationPermission
                        audioPermission = tempAudioPermission
                        cameraPermission = tempCameraPermission
                        showPrivacyDialog = false
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
            },
            title = { Text("Gizlilik & Güvenlik") },
            text = {
                Column {
                    Text("Kişisel verileriniz uygulama dışında paylaşılmaz ve güvenli bir şekilde saklanır.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("İzinler sadece uygulamanın temel işlevleri için kullanılır.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Daha fazla bilgi için bizimle iletişime geçebilirsiniz.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bildirim İzni", modifier = Modifier.weight(1f))
                        Switch(
                            checked = tempNotificationPermission,
                            onCheckedChange = { tempNotificationPermission = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ses İzni", modifier = Modifier.weight(1f))
                        Switch(
                            checked = tempAudioPermission,
                            onCheckedChange = { tempAudioPermission = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Kamera İzni", modifier = Modifier.weight(1f))
                        Switch(
                            checked = tempCameraPermission,
                            onCheckedChange = { tempCameraPermission = it }
                        )
                    }
                }
            }
        )
    }

    if (showDevicesDialog) {
        AlertDialog(
            onDismissRequest = { showDevicesDialog = false },
            confirmButton = {
                Button(
                    onClick = { showDevicesDialog = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Kaydet"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kaydet")
                }
            },
            title = { Text("Bağlı Cihazlar") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { /* Cihaz 1 detaylarına git */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Cihaz 1", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                    Button(
                        onClick = { /* Yeni cihaz ekle */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Yeni Cihaz Ekle +", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        )
    }
}

@Composable
fun HelpMenu(
    onDismiss: () -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    var showEmailInfo by remember { mutableStateOf(false) }
    var showSystemStatus by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Yardım & Destek") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showEmailInfo = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
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
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Text("Canlı Destek")
                }
                Button(
                    onClick = { showSystemStatus = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Text("Sistem Durumu")
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

    if (showSystemStatus) {
        AlertDialog(
            onDismissRequest = { showSystemStatus = false },
            confirmButton = {
                TextButton(onClick = { showSystemStatus = false }) {
                    Text("Kapat")
                }
            },
            title = { Text("Sistem Durumu") },
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(Color(0xFF4CAF50))
                    )
                    Text("Sistem şu anda sorunsuz ve aktif olarak çalışıyor.")
                }
            }
        )
    }
}
