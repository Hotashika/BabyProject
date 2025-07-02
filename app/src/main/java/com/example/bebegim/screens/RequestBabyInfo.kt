package com.example.bebegim.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bebegim.R
import com.example.bebegim.auth.AuthViewModel
import com.example.bebegim.ui.components.LoadingButton
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun RequestBabyInfo(
    signupFullName: String,
    signupEmail: String,
    signupPassword: String,
    onBabyInfoSubmitted: (Boolean) -> Unit,
    onBabyInfoCancelled: (Boolean) -> Unit,
    onSignupSuccess: (String, String, String) -> Unit,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
) {
    var babyFullName by remember { mutableStateOf("") }
    var babyGender by remember { mutableStateOf("") }
    var babyBirthDate by remember { mutableStateOf("") }
    var babyBloodType by remember { mutableStateOf("") }
    var babyWeight by remember { mutableStateOf("") }
    var babyHeight by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLoadingRegister = authViewModel.isLoadingRegister

    // AuthViewModel'den gelen hata mesajlarını dinleyin
    LaunchedEffect(authViewModel.errorMessage) {
        authViewModel.errorMessage?.let { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                // Hata mesajını temizle
                authViewModel.errorMessage = null
            }
        }
    }

    val isDark = isSystemInDarkTheme()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkPastelBlue else PastelBlueWhite)
            .padding(24.dp)
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "Bebek Bilgileri",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        BabyFullNameTextField(
            value = babyFullName,
            onValueChange = { babyFullName = it },
            label = "Bebeğinizin Adı",
            placeholder = "Bebeğinizin Adını Giriniz",
            iconRes = R.drawable.user_24,
            keyboardType = KeyboardType.Text
        )

        BabyGenderTextField(
            value = babyGender,
            onValueChange = { babyGender = it },
            label = "Cinsiyet",
            placeholder = "Cinsiyet Seçiniz"
        )

        BabyBirthDateTextField(
            value = babyBirthDate,
            onValueChange = { babyBirthDate = it },
            label = "Doğum Tarihi",
            placeholder = "GG/AA/YYYY",
            iconRes = R.drawable.cake_birthday_20
        )

        BabyWeightTextField(
            value = babyWeight,
            onValueChange = { babyWeight = it },
            label = "Kilo (kg)",
            placeholder = "Bebeğinizin kilosunu giriniz",
            iconRes = R.drawable.scale_20,
            keyboardType = KeyboardType.Decimal
        )

        BabyHeightTextField(
            value = babyHeight,
            onValueChange = { babyHeight = it },
            label = "Boy (cm)",
            placeholder = "Bebeğinizin boyunu giriniz",
            iconRes = R.drawable.measuring_tape_20,
            keyboardType = KeyboardType.Decimal
        )

        BabyBloodTypeDropDownField(
            value = babyBloodType,
            onValueChange = { babyBloodType = it },
            label = "Kan Grubu",
            iconRes = R.drawable.blood_20
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("İptal")
            }

            LoadingButton(
                text = "Kaydı Tamamla",
                isLoading = isLoadingRegister,
                onClick = {
                    // Debug için log ekleyin
                    println("=== KAYIT TAMAMLA BASILDI ===")
                    println("Bebek Adı: '$babyFullName'")
                    println("Cinsiyet: '$babyGender'")
                    println("Doğum Tarihi: '$babyBirthDate'")
                    println("Kan Grubu: '$babyBloodType'")
                    println("Kilo: '$babyWeight'")
                    println("Boy: '$babyHeight'")

                    // Trim işlemi yaparak boşlukları temizleyin
                    val trimmedName = babyFullName.trim()
                    val trimmedGender = babyGender.trim()
                    val trimmedBirthDate = babyBirthDate.trim()
                    val trimmedBloodType = babyBloodType.trim()
                    val trimmedWeight = babyWeight.trim()
                    val trimmedHeight = babyHeight.trim()

                    if (trimmedName.isNotEmpty() &&
                        trimmedBirthDate.isNotEmpty() &&
                        trimmedGender.isNotEmpty() &&
                        trimmedBloodType.isNotEmpty() &&
                        trimmedWeight.isNotEmpty() &&
                        trimmedHeight.isNotEmpty()) {

                        println("Tüm alanlar dolu, kayıt işlemi başlatılıyor...")

                        // Burada bebek bilgilerini de kaydetmeniz gerekebilir
                        // Önce bebek bilgilerini kaydedin, sonra kullanıcı kaydını yapın
                        authViewModel.signUpNewUser(
                            email = signupEmail,
                            password = signupPassword,
                            onSuccess = {
                                println("Kullanıcı kaydı başarılı!")
                                // TODO: Burada bebek bilgilerini de veritabanına kaydedin
                                // authViewModel.saveBabyInfo(trimmedName, trimmedGender, trimmedBirthDate, trimmedBloodType, trimmedWeight, trimmedHeight)

                                onSignupSuccess(signupFullName, signupEmail, signupPassword)
                                onBabyInfoSubmitted(true)
                                Toast.makeText(context, "Kayıt başarıyla tamamlandı!", Toast.LENGTH_SHORT).show()
                            },
                            onError = { errorMessage ->
                                println("Kayıt hatası: $errorMessage")
                                authViewModel.errorMessage = errorMessage ?: "Kayıt başarısız. Lütfen tekrar deneyiniz."
                            }
                        )
                    } else {
                        println("Eksik alanlar var!")
                        val missingFields = mutableListOf<String>()
                        if (trimmedName.isEmpty()) missingFields.add("Bebek Adı")
                        if (trimmedGender.isEmpty()) missingFields.add("Cinsiyet")
                        if (trimmedBirthDate.isEmpty()) missingFields.add("Doğum Tarihi")
                        if (trimmedBloodType.isEmpty()) missingFields.add("Kan Grubu")
                        if (trimmedWeight.isEmpty()) missingFields.add("Kilo")
                        if (trimmedHeight.isEmpty()) missingFields.add("Boy")

                        val missingFieldsText = missingFields.joinToString(", ")
                        Toast.makeText(context, "Lütfen şu alanları doldurun: $missingFieldsText", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        if (showDialog) {
            AlertDialogComponent(
                onDismiss = { showDialog = false },
                authViewModel = authViewModel,
                context = context,
                coroutineScope = coroutineScope,
                onLogout = onLogout,
                onBabyInfoCancelled = onBabyInfoCancelled
            )
        }
    }
}

// Diğer composable fonksiyonları aynı kalacak...
@Composable
private fun BabyFullNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 0.dp)
                .padding(start = 16.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (value.isEmpty()) {
                    Text(placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            },
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
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )
    }
}

@Composable
private fun BabyGenderTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    val currentValue = value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 0.dp)
                .padding(start = 16.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { onValueChange("Kız") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentValue == "Kız") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (currentValue == "Kız") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Kız")
            }
            Button(
                onClick = { onValueChange("Erkek") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentValue == "Erkek") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (currentValue == "Erkek") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Erkek")
            }
        }
    }
}

@Composable
private fun BabyBirthDateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()

        if (value.isNotEmpty()) {
            try {
                val dateParts = value.split("/")
                if (dateParts.size == 3) {
                    val day = dateParts[0].toInt()
                    val month = dateParts[1].toInt() - 1
                    val year = dateParts[2].toInt()
                    calendar.set(year, month, day)
                }
            } catch (e: Exception) {
                calendar.time = Date()
            }
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onValueChange(formattedDate)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener { showDatePicker = false }
            datePicker.maxDate = System.currentTimeMillis()
            val minCalendar = Calendar.getInstance()
            minCalendar.add(Calendar.YEAR, -10)
            datePicker.minDate = minCalendar.timeInMillis
        }.show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 0.dp)
                .padding(start = 16.dp),
        )

        OutlinedTextField(
            value = value,
            onValueChange = {},
            placeholder = {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = "$label icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_calendar_month_24),
                    contentDescription = "Takvim aç",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            singleLine = true,
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .clickable { showDatePicker = true }
        )
    }
}

@Composable
private fun BabyWeightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 0.dp)
                .padding(start = 16.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (value.isEmpty()) {
                    Text(placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            },
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
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )
    }
}

@Composable
private fun BabyHeightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 0.dp)
                .padding(start = 16.dp),
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (value.isEmpty()) {
                    Text(placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            },
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
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyBloodTypeDropDownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    iconRes: Int
) {
    val items = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, bottom = 0.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                readOnly = true,
                value = value,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "$label icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                placeholder = {
                    Text(
                        text = "Kan grubu seçiniz",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { selectedItem ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectedItem,
                                color = if (selectedItem == value)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onValueChange(selectedItem)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertDialogComponent(
    onDismiss: () -> Unit,
    authViewModel: AuthViewModel,
    context: Context,
    coroutineScope: CoroutineScope,
    onLogout: () -> Unit,
    onBabyInfoCancelled: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .wrapContentHeight()
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "Kayıt İptal Et",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Kaydınızı iptal etmek istediğinizden emin misiniz? Bu işlem geri alınamaz.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline
                        )
                    ) {
                        Text(
                            text = "Hayır",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                authViewModel.logout()
                                onLogout()
                            }
                            onBabyInfoCancelled(true)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = "Evet",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}