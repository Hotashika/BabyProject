package com.example.bebegim.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class GetVideo {
    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)      // 5 yerine 30 saniye
        .connectTimeout(10, TimeUnit.SECONDS)   // 3 yerine 10 saniye
        .callTimeout(0, TimeUnit.SECONDS)       // 0 = sınırsız, video akışı için ideal
        .build()

    private val videoStreamUrl = "http://172.20.32.72:5000/video" // Gerekirse IP'yi güncelle

    fun getVideoStream(): Flow<Bitmap?> = flow {
        try {
            val request = Request.Builder()
                .url(videoStreamUrl)
                .addHeader("Cache-Control", "no-cache")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    emit(null)
                    return@flow
                }

                val inputStream = response.body?.byteStream()
                if (inputStream == null) {
                    emit(null)
                    return@flow
                }

                readMjpegStreamOptimized(inputStream) { bitmap ->
                    emit(bitmap)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun readMjpegStreamOptimized(
        inputStream: InputStream,
        onFrameReady: suspend (Bitmap?) -> Unit
    ) {
        val buffer = ByteArray(16384)
        val frameBuffer = ByteArrayOutputStream()
        var boundaryFound = false
        val boundary = "--frame"
        var frameCount = 0
        val maxFrameSize = 1024 * 1024 // 1MB

        try {
            while (kotlinx.coroutines.currentCoroutineContext().isActive) {
                val bytesRead = inputStream.read(buffer)
                if (bytesRead == -1) break

                if (frameBuffer.size() > maxFrameSize) {
                    frameBuffer.reset()
                    boundaryFound = false
                    continue
                }

                val data = String(buffer, 0, bytesRead, Charsets.ISO_8859_1)

                if (data.contains(boundary)) {
                    if (boundaryFound && frameBuffer.size() > 0) {
                        frameCount++
                        if (frameCount % 2 == 0) { // Her 2. frame’i işliyoruz
                            processFrame(frameBuffer.toByteArray(), onFrameReady)
                        }
                        frameBuffer.reset()
                    }
                    boundaryFound = true
                    val boundaryIndex = data.indexOf(boundary)
                    if (boundaryIndex != -1) {
                        val afterBoundary = data.substring(boundaryIndex + boundary.length)
                        frameBuffer.write(afterBoundary.toByteArray(Charsets.ISO_8859_1))
                    }
                } else if (boundaryFound) {
                    frameBuffer.write(buffer, 0, bytesRead)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onFrameReady(null)
        }
    }

    private suspend fun processFrame(
        frameBytes: ByteArray,
        onFrameReady: suspend (Bitmap?) -> Unit
    ) {
        try {
            val headerEndIndex = findHeaderEnd(frameBytes)
            if (headerEndIndex != -1 && headerEndIndex < frameBytes.size) {
                val imageBytes = frameBytes.copyOfRange(headerEndIndex, frameBytes.size)
                val options = BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.RGB_565
                    inSampleSize = 1
                    inPurgeable = true
                    inInputShareable = true
                }

                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
                onFrameReady(bitmap)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun findHeaderEnd(data: ByteArray): Int {
        val headerEnd = "\r\n\r\n".toByteArray()
        for (i in 0..data.size - headerEnd.size) {
            var match = true
            for (j in headerEnd.indices) {
                if (data[i + j] != headerEnd[j]) {
                    match = false
                    break
                }
            }
            if (match) return i + headerEnd.size
        }
        return -1
    }

    suspend fun getSingleFrame(): Bitmap? {
        return try {
            val request = Request.Builder()
                .url(videoStreamUrl)
                .addHeader("Cache-Control", "no-cache")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null

                val bytes = response.body?.bytes() ?: return null

                val options = BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.RGB_565
                    inSampleSize = 1
                }

                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
