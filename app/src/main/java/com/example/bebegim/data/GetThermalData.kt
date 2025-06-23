package com.example.bebegim.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class GetThermalData {
    private val TAG = "ThermalAPI"

    suspend fun fetchMeanTemperature(): Double? = withContext(Dispatchers.IO) {
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

            val centerTemp = json.getDouble("max_temperature")
            Log.d(TAG, "Center Temperature: $centerTemp")

            return@withContext centerTemp
        } catch (e: Exception) {
            Log.e(TAG, "API çağrısı başarısız: ${e.message}")
            return@withContext null
        }
    }
}
