package com.example.bebegim.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class ThermalData(
    val maxTemperature: Double,
    val minTemperature: Double,
    val meanTemperature: Double,
    val centerTemperature: Double,
    val thermalImageBase64: String?,
    val shape: ThermalShape
)

data class ThermalShape(
    val rows: Int,
    val cols: Int
)

class GetThermalData {
    private val TAG = "ThermalAPI"

    suspend fun fetchMeanTemperature(): Double? = withContext(Dispatchers.IO) {
        try {
            val thermalData = fetchThermalData()
            return@withContext thermalData?.maxTemperature
        } catch (e: Exception) {
            Log.e(TAG, "Sıcaklık verisi alınamadı: ${e.message}")
            return@withContext null
        }
    }

    suspend fun fetchThermalData(): ThermalData? = withContext(Dispatchers.IO) {
        try {
            val url = "http://10.0.2.2:8000/thermal"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(TAG, "HTTP error code: ${response.code}")
                return@withContext null
            }

            val body = response.body?.string() ?: return@withContext null
            Log.d(TAG, "Raw JSON: $body")
            val json = JSONObject(body)

            val maxTemp = json.getDouble("max_temperature")
            val minTemp = json.getDouble("min_temperature")
            val meanTemp = json.getDouble("mean_temperature")
            val centerTemp = json.getDouble("center_temperature")
            val thermalImage = json.optString("thermal_image", null)

            val shapeJson = json.getJSONObject("shape")
            val shape = ThermalShape(
                rows = shapeJson.getInt("rows"),
                cols = shapeJson.getInt("cols")
            )

            Log.d(TAG, "Thermal Data - Max: $maxTemp, Min: $minTemp, Mean: $meanTemp")

            return@withContext ThermalData(
                maxTemperature = maxTemp,
                minTemperature = minTemp,
                meanTemperature = meanTemp,
                centerTemperature = centerTemp,
                thermalImageBase64 = thermalImage,
                shape = shape
            )
        } catch (e: Exception) {
            Log.e(TAG, "API çağrısı başarısız: ${e.message}")
            return@withContext null
        }
    }

    suspend fun fetchThermalImageUrl(): String? = withContext(Dispatchers.IO) {
        try {
            // Timestamp ekleyerek cache'i bypass et
            val timestamp = System.currentTimeMillis()
            return@withContext "http://10.0.2.2:8000/thermal/image?t=$timestamp"
        } catch (e: Exception) {
            Log.e(TAG, "Görüntü URL'i alınamadı: ${e.message}")
            return@withContext null
        }
    }
}