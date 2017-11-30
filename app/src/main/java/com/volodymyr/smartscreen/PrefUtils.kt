package com.volodymyr.smartscreen

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils


class PrefUtils(private val context: Context) {
    /**
     * Helper method to retrieve a boolean value from [SharedPreferences].
     *
     * @param key
     * @return The value from shared preferences, or the provided default.
     */
    fun getBoolean(key: String): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }

    /**
     * Helper method to write a boolean value to [SharedPreferences].
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setBoolean(key: String, value: Boolean): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a String value from [SharedPreferences].
     *
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    fun getString(key: String, defaultString: String): String? {
        var value: String = defaultString
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getString(key, defaultString)
        }
        return value
    }

    /**
     * Helper method to write a String value to [SharedPreferences].
     *
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setString(key: String, value: String): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            editor.putString(key, value)
            return editor.commit()
        }
        return false
    }
}