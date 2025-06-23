package com.example.bebegim.data

import androidx.compose.runtime.mutableStateListOf

class ListData {
    private val maxSize = 7
    private val temperatureList = mutableStateListOf<Float>()

    fun addTemperature(temp: Double) {
        if (temperatureList.size == maxSize) {
            temperatureList.removeAt(0)
        }
        temperatureList.add(temp.toFloat())
    }

    fun getTemperatures(): List<Float> {
        return temperatureList.toList()
    }
}
