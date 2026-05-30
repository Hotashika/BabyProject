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
    val shape: ThermalShape,
    val anatomicalAnalysis: AnatomicalAnalysis?
)

data class ThermalShape(
    val rows: Int,
    val cols: Int
)

data class AnatomicalAnalysis(
    val headTemperature: Double,
    val chestTemperature: Double,
    val lowerBodyTemperature: Double,
    val healthAssessment: HealthAssessment,
    val regionStatistics: RegionStatistics
)

data class HealthAssessment(
    val overall: String,
    val warnings: List<String>,
    val recommendations: List<String>
)

data class RegionStatistics(
    val head: RegionStats,
    val chest: RegionStats,
    val lower: RegionStats
)

data class RegionStats(
    val max: Double,
    val min: Double,
    val mean: Double,
    val std: Double
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

    suspend fun fetchHeadUpLowBodyTemperature(): Triple<Double, Double, Double>? = withContext(Dispatchers.IO) {
        try {
            val thermalData = fetchThermalData()
            val anatomical = thermalData?.anatomicalAnalysis

            if (anatomical != null) {
                return@withContext Triple(
                    anatomical.headTemperature,
                    anatomical.chestTemperature,
                    anatomical.lowerBodyTemperature
                )
            }

            Log.w(TAG, "Anatomik analiz verisi bulunamadı")
            return@withContext null
        } catch (e: Exception) {
            Log.e(TAG, "Anatomik sıcaklık verisi alınamadı: ${e.message}")
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

            var anatomicalAnalysis: AnatomicalAnalysis? = null
            if (json.has("anatomical_analysis")) {
                try {
                    val anatomicalJson = json.getJSONObject("anatomical_analysis")

                    val headTemp = anatomicalJson.getDouble("head_temperature")
                    val chestTemp = anatomicalJson.getDouble("chest_temperature")
                    val lowerTemp = anatomicalJson.getDouble("lower_body_temperature")

                    val healthJson = anatomicalJson.getJSONObject("health_assessment")
                    val overall = healthJson.getString("overall")
                    val warningsArray = healthJson.getJSONArray("warnings")
                    val recommendationsArray = healthJson.getJSONArray("recommendations")

                    val warnings = mutableListOf<String>()
                    for (i in 0 until warningsArray.length()) {
                        warnings.add(warningsArray.getString(i))
                    }

                    val recommendations = mutableListOf<String>()
                    for (i in 0 until recommendationsArray.length()) {
                        recommendations.add(recommendationsArray.getString(i))
                    }

                    val healthAssessment = HealthAssessment(overall, warnings, recommendations)

                    // Region statistics parsing
                    val regionStatsJson = anatomicalJson.getJSONObject("region_statistics")

                    fun parseRegionStats(regionJson: JSONObject): RegionStats {
                        return RegionStats(
                            max = regionJson.getDouble("max"),
                            min = regionJson.getDouble("min"),
                            mean = regionJson.getDouble("mean"),
                            std = regionJson.getDouble("std")
                        )
                    }

                    val headStats = parseRegionStats(regionStatsJson.getJSONObject("head"))
                    val chestStats = parseRegionStats(regionStatsJson.getJSONObject("chest"))
                    val lowerStats = parseRegionStats(regionStatsJson.getJSONObject("lower"))

                    val regionStatistics = RegionStatistics(headStats, chestStats, lowerStats)

                    anatomicalAnalysis = AnatomicalAnalysis(
                        headTemperature = headTemp,
                        chestTemperature = chestTemp,
                        lowerBodyTemperature = lowerTemp,
                        healthAssessment = healthAssessment,
                        regionStatistics = regionStatistics
                    )

                    Log.d(TAG, "Anatomical Data - Head: $headTemp, Chest: $chestTemp, Lower: $lowerTemp")
                } catch (e: Exception) {
                    Log.w(TAG, "Anatomical analysis parsing failed: ${e.message}")
                }
            }

            Log.d(TAG, "Thermal Data - Max: $maxTemp, Min: $minTemp, Mean: $meanTemp")

            return@withContext ThermalData(
                maxTemperature = maxTemp,
                minTemperature = minTemp,
                meanTemperature = meanTemp,
                centerTemperature = centerTemp,
                thermalImageBase64 = thermalImage,
                shape = shape,
                anatomicalAnalysis = anatomicalAnalysis
            )
        } catch (e: Exception) {
            Log.e(TAG, "API çağrısı başarısız: ${e.message}")
            return@withContext null
        }
    }

    suspend fun fetchThermalImageUrl(): String? = withContext(Dispatchers.IO) {
        try {
            val timestamp = System.currentTimeMillis()
            return@withContext "http://10.0.2.2:8000/thermal/image?t=$timestamp"
        } catch (e: Exception) {
            Log.e(TAG, "Görüntü URL'i alınamadı: ${e.message}")
            return@withContext null
        }
    }
}