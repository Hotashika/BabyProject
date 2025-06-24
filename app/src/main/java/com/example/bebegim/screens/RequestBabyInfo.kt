package com.example.bebegim.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bebegim.R
import com.example.bebegim.ui.theme.DarkPastelBlue
import com.example.bebegim.ui.theme.PastelBlueWhite
import io.ktor.websocket.Frame.Text

@Composable
fun RequestBabyInfo(
    onBabyInfoSubmitted: (Boolean) -> Unit,
    onBabyInfoCancelled: (Boolean) -> Unit
){
    var babyFullName by remember { mutableStateOf("") }
    var babyGender by remember { mutableStateOf("") }
    var babyBirthDate by remember { mutableStateOf("") }
    var babyBloodType by remember { mutableStateOf("") }
    var babyWeight by remember { mutableStateOf("") }
    var babyHeight by remember { mutableStateOf("") }

    val isDark = isSystemInDarkTheme()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) DarkPastelBlue else PastelBlueWhite)
            .padding(24.dp)
            .padding(bottom = 60.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
    ){
        Text(
            text = "Bebek Bilgileri",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            placeholder = "Cinsiyet Seçiniz",
            iconRes = R.drawable.user_24
        )

        BabyBirthDateTextField(
            value = babyBirthDate,
            onValueChange = { babyBirthDate = it },
            label = "Doğum Tarihi",
            placeholder = "GG/AA/YYYY",
            iconRes = R.drawable.user_24,
            keyboardType = KeyboardType.Number
        )

        BabyBloodTypeTextField(
            value = babyBloodType,
            onValueChange = { babyBloodType = it },
            label = "Kan Grubu",
            placeholder = "Kan Grubu Seçiniz",
            iconRes = R.drawable.user_24,
            keyboardType = KeyboardType.Text
        )

        BabyWeightTextField(
            value = babyWeight,
            onValueChange = { babyWeight = it },
            label = "Kilo (kg)",
            placeholder = "Bebeğinizin kilosunu giriniz",
            iconRes = R.drawable.user_24,
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { onBabyInfoCancelled(true) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("İptal")
            }

            Button(
                onClick = { onBabyInfoSubmitted(true) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Kaydet")
            }
        }

    }
}

@Composable
private fun BabyFullNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType
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
            /*label = { Text(label, color = MaterialTheme.colorScheme.onSurface) },*/
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
    val expanded = remember { mutableStateOf(false) }
    val internalValue = remember { mutableStateOf(value) }
    val genderOptions = listOf("Erkek", "Kız")
    val currentValue = if (onValueChange != null) value else internalValue.value

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = true }
        ) {
            OutlinedTextField(
                value = currentValue,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.dropdown_select_24),
                        contentDescription = "Dropdown Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
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

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = gender,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        expanded.value = false
                        if (onValueChange != null) {
                            onValueChange(gender)
                        } else {
                            internalValue.value = gender
                        }
                    }
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
    keyboardType: KeyboardType
) {
    // Implement as needed
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
    // Implement as needed
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
    // Implement as needed
}

@Composable
private fun BabyBloodTypeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType
) {
    // Implement as needed
}

@Preview
@Composable
fun RequestBabyInfoPreview() {
    RequestBabyInfo(
        onBabyInfoSubmitted = {},
        onBabyInfoCancelled = {}
    )
}