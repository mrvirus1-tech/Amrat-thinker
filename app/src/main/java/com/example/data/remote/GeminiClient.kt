package com.example.data.remote

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Volatile
    private var cachedApiKey: String = ""

    fun updateCachedApiKey(key: String) {
        cachedApiKey = key.trim()
    }

    fun hasValidApiKey(): Boolean {
        if (cachedApiKey.isNotBlank()) return true
        val key = BuildConfig.GEMINI_API_KEY
        return !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
    }

    fun getActiveApiKey(): String {
        if (cachedApiKey.isNotBlank()) return cachedApiKey
        val key = BuildConfig.GEMINI_API_KEY
        if (!key.isNullOrBlank() && key != "MY_GEMINI_API_KEY") return key
        return ""
    }

    suspend fun testConnection(apiKey: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val rootJson = JSONObject()
            val partsArray = JSONArray().put(JSONObject().put("text", "Respond with 'Online OK'"))
            rootJson.put("contents", JSONArray().put(JSONObject().put("parts", partsArray)))

            val requestBody = rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=${apiKey.trim()}")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorMsg = try {
                    JSONObject(responseBody).optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext Result.failure(Exception(errorMsg))
            }

            Result.success("Connection verified! Gemini 3.5 Flash is active and online.")
        } catch (e: Exception) {
            Result.failure(Exception(e.localizedMessage ?: "Network connection failed"))
        }
    }

    suspend fun generateContent(
        prompt: String,
        systemInstruction: String = "You are Amrt Thinker, an advanced AI education and notes assistant designed for students. Be concise, highly structured, encouraging, and accurate.",
        imageBitmap: Bitmap? = null,
        apiKeyOverride: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyOverride?.takeIf { it.isNotBlank() } ?: getActiveApiKey()

        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                Exception("Gemini API key is required for online AI operations. Please configure it in Profile -> AI Settings.")
            )
        }

        try {
            val rootJson = JSONObject()

            // System Instruction
            val sysPart = JSONObject().put("text", systemInstruction)
            val sysContent = JSONObject().put("parts", JSONArray().put(sysPart))
            rootJson.put("systemInstruction", sysContent)

            // Contents array
            val partsArray = JSONArray()
            partsArray.put(JSONObject().put("text", prompt))

            // Multimodal image support
            if (imageBitmap != null) {
                val outputStream = ByteArrayOutputStream()
                // Compress nicely for network transmission
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                val base64Data = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                val inlineData = JSONObject()
                    .put("mimeType", "image/jpeg")
                    .put("data", base64Data)
                partsArray.put(JSONObject().put("inlineData", inlineData))
            }

            val contentsArray = JSONArray()
            contentsArray.put(JSONObject().put("parts", partsArray))
            rootJson.put("contents", contentsArray)

            // Generation config
            val genConfig = JSONObject()
                .put("temperature", 0.7)
                .put("topP", 0.95)
            rootJson.put("generationConfig", genConfig)

            val requestBody = rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorMsg = try {
                    val errObj = JSONObject(responseBody).optJSONObject("error")
                    errObj?.optString("message") ?: "HTTP ${response.code}: $responseBody"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext Result.failure(Exception("Gemini Online Error: $errorMsg"))
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val contentObj = firstCandidate?.optJSONObject("content")
            val parts = contentObj?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                Result.success(text)
            } else {
                Result.failure(Exception("Gemini returned an empty response. Please try with different input."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gemini network error: ${e.localizedMessage ?: "Check your internet connection"}"))
        }
    }
}
