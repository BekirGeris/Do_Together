package com.example.dotogether.util

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    private var INSTANCE: SharedPreferences? = null

    private fun getInstance(context: Context): SharedPreferences {
        if (INSTANCE == null) {
            INSTANCE = context.getSharedPreferences("NAME", Context.MODE_PRIVATE)
        }
        return INSTANCE!!
    }

    fun setString(context: Context, key: String, value: String) {
        getInstance(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String): String {
        return getInstance(context).getString(key, defaultValue).toString()
    }

    fun setLong(context: Context, key: String, value: Long) {
        getInstance(context).edit().putLong(key, value).apply()
    }

    fun getLong(context: Context, key: String, defaultValue: Long): Long {
        return getInstance(context).getLong(key, defaultValue)
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        getInstance(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getInstance(context).getBoolean(key, defaultValue)
    }
}