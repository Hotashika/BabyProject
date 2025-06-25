package com.example.bebegim.model

data class VitalData(
    val type: VitalType,
    val value: String,
    val status: String,
    val isNormal: Boolean
)

enum class VitalType(val displayName: String) {
    BABY_TEMPERATURE("Bebek Sıcaklığı"),
    ROOM_TEMPERATURE("Oda Sıcaklığı"),
    HUMIDITY("Nem"),
    SLEEP("Uyku"),
    CO2("Hava Kalitesi"),
    BABY_HEAD_TEMPERATURE("Bebek Baş Sıcaklığı"),
    BABY_UPPERBODY_TEMPERATURE("Bebek Vücut Sıcaklığı"),
    BABY_LOWERBODY_TEMPERATURE("Bebek Alt Vücut Sıcaklığı"),
}
