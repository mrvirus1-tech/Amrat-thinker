package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

object AppPreferences {
    private const val PREFS_NAME = "amrt_thinker_prefs"
    private const val KEY_CUSTOM_API_KEY = "custom_gemini_api_key"
    private const val KEY_SELECTED_DURATION_MINS = "selected_quiz_duration_mins"
    private const val KEY_SELECTED_QUESTION_COUNT = "selected_quiz_question_count"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getCustomApiKey(context: Context): String {
        return getPrefs(context).getString(KEY_CUSTOM_API_KEY, "") ?: ""
    }

    fun setCustomApiKey(context: Context, apiKey: String) {
        getPrefs(context).edit().putString(KEY_CUSTOM_API_KEY, apiKey.trim()).apply()
    }

    fun getEffectiveApiKey(context: Context): String {
        val custom = getCustomApiKey(context)
        if (custom.isNotBlank()) {
            return custom
        }
        val buildKey = BuildConfig.GEMINI_API_KEY
        if (!buildKey.isNullOrBlank() && buildKey != "MY_GEMINI_API_KEY") {
            return buildKey
        }
        return ""
    }

    fun hasApiKey(context: Context): Boolean {
        return getEffectiveApiKey(context).isNotBlank()
    }

    fun getSavedQuizDurationMins(context: Context): Int {
        return getPrefs(context).getInt(KEY_SELECTED_DURATION_MINS, 30) // default 30 mins
    }

    fun setSavedQuizDurationMins(context: Context, minutes: Int) {
        getPrefs(context).edit().putInt(KEY_SELECTED_DURATION_MINS, minutes.coerceIn(10, 120)).apply()
    }

    fun getSavedQuizQuestionCount(context: Context): Int {
        return getPrefs(context).getInt(KEY_SELECTED_QUESTION_COUNT, 20) // default 20 questions
    }

    fun setSavedQuizQuestionCount(context: Context, count: Int) {
        getPrefs(context).edit().putInt(KEY_SELECTED_QUESTION_COUNT, count.coerceIn(20, 1024)).apply()
    }
}
