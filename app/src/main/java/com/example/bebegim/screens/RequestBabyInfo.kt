package com.example.bebegim.screens
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.bebegim.room.AppDatabase
import com.example.bebegim.room.Babies
import com.example.bebegim.room.BabiesData
import com.example.bebegim.ui.components.LoadingButton
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import com.example.bebegim.ui.theme.Poppins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

@Composable
fun BirthDatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "$label icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Takvim aç",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { showDatePicker = true }
            )
        },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    )

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onValueChange(selectedDate)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

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
){
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

    val db = remember {
        AppDatabase.getInstance(context)
    }
    val usersDao = db.UsersDao()
    val babiesDao = db.BabiesDao()
    val babiesDataDao = db.BabiesDataDao()

    val isDark = isSystemInDarkTheme()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkPastelBlue else PastelBlueWhite)
            .padding(24.dp)
            .padding(top = 24.dp)
            /*            .padding(bottom = 60.dp)*/
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
    ){
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
            value = babyFullName.uppercase(),
            onValueChange = { babyFullName = it },
            label = "Bebeğinizin Adı",
            shape = RoundedCornerShape(16.dp),
            placeholder = "Bebeğinizin Adını Giriniz",
            iconRes = R.drawable.user_24,
            keyboardType = KeyboardType.Text
        )

        BabyGenderTextField(
            value = babyGender,
            onValueChange = { babyGender = it },
            label = "Cinsiyet",
            placeholder = "Cinsiyet Seçiniz",
        )
        BirthDatePickerField(
            value = babyBirthDate,
            onValueChange = { babyBirthDate = it },
            label = "Doğum Tarihi",
            shape = RoundedCornerShape(16.dp),
            placeholder = "GG/AA/YYYY",
            iconRes = R.drawable.cake_birthday_20
        )


        BabyWeightTextField(
            value = babyWeight,
            onValueChange = { babyWeight = it },
            label = "Kilo (kg)",
            shape = RoundedCornerShape(16.dp),
            placeholder = "Bebeğinizin kilosunu giriniz",
            iconRes = R.drawable.scale_20,
            keyboardType = KeyboardType.Decimal
        )

        BabyHeightTextField(
            value = babyHeight,
            onValueChange = { babyHeight = it },
            label = "Boy (cm)",
            shape = RoundedCornerShape(16.dp),
            placeholder = "Bebeğinizin boyunu giriniz",
            iconRes = R.drawable.measuring_tape_20,
            keyboardType = KeyboardType.Decimal
        )

        BabyBloodTypeDropDownField(
            value = babyBloodType,
            onValueChange = { babyBloodType = it },
            label = "Kan Grubu"
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    showDialog = true
                },
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
                    if (babyFullName.isNotEmpty() &&  babyGender.isNotEmpty() && babyBirthDate.isNotEmpty()) {
                        authViewModel.signUpNewUser(
                            email = signupEmail,
                            password = signupPassword,
                            fullName = signupFullName.uppercase(),
                            onSuccess = {
                                onSignupSuccess(signupFullName, signupEmail, signupPassword)
                                onBabyInfoSubmitted(true)
                                coroutineScope.launch {
                                    val user = usersDao.getUserByEmail(signupEmail)
                                    if (user != null) {
                                        val babies = Babies(
                                            babyId = UUID.randomUUID().toString(),
                                            userId = user.userId,
                                            name = babyFullName,
                                            birthDate = babyBirthDate,
                                            gender = babyGender,
                                            currentWeight = babyWeight.toDoubleOrNull(),
                                            currentHeight = babyHeight.toDoubleOrNull(),
                                            bloodType = babyBloodType,
                                            createdAt = java.time.Instant.now().toString(),
                                            updatedAt = null,
                                        )
                                        val babiesData = BabiesData(
                                            dataId = UUID.randomUUID().toString(),
                                            babyId = babies.babyId,
                                            weightHistory = listOf(babyWeight.toDoubleOrNull() ?: 0.0),
                                            heightHistory = listOf(babyHeight.toDoubleOrNull() ?: 0.0),
                                            createdAt = java.time.Instant.now().toString(),
                                            updatedAt = null
                                        )
                                        babiesDao.insert(babies)
                                        babiesDataDao.insert(babiesData)
                                    }
                                }
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            )
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
}
@Composable
private fun BabyBirthDateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType,
    shape: RoundedCornerShape
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    OutlinedTextField(
        value = value,
        onValueChange = {},
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
        readOnly = true,
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
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    val datePicker = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                            onValueChange(selectedDate)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                })
            }
    )
}
@Composable
private fun BabyFullNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType,
    shape: RoundedCornerShape
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ){
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
    value: String = "",
    onValueChange: ((String) -> Unit)? = null,
    label: String = "Cinsiyet",
    placeholder: String = "Cinsiyet seçiniz",
    iconRes: Int = R.drawable.profile_placeholder,
) {
    val currentValue = value
    val onChange = onValueChange ?: {}

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
                onClick = { onChange("Kız") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentValue == "Kız") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (currentValue == "Kız") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Kız")
            }
            Button(
                onClick = { onChange("Erkek") },
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
private fun BabyWeightTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType,
    shape: RoundedCornerShape
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
    keyboardType: KeyboardType,
    shape: RoundedCornerShape
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
    label: String
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
                placeholder = {
                    Text(
                        text = "Kan grubu seçiniz",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                /*shape = RoundedCornerShape(16.dp),*/
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

@Composable
fun HideKeyboardOnTapAlternative(
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        content()
    }
}