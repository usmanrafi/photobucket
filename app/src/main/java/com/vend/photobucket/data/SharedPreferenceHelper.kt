package com.vend.photobucket.data

import android.content.SharedPreferences
import com.vend.photobucket.application.PhotoApplication
import javax.inject.Inject

class SharedPreferenceHelper(var sharedPreferences: SharedPreferences) {

    private val KEY = "phoneNumber"

    fun putSession(phoneNumber: String) {
        sharedPreferences.edit().putString(this.KEY, phoneNumber).apply()
    }

    fun getSession() = sharedPreferences.getString(this.KEY, "null")!!

    fun clearSession() {
//        sharedPreferences.edit().putString(this.KEY, "null").apply()
        sharedPreferences.edit().remove(this.KEY).apply()
    }

}