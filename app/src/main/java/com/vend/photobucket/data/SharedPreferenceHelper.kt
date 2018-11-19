package com.vend.photobucket.data

import android.content.SharedPreferences
import com.vend.photobucket.utils.Keys

class SharedPreferenceHelper(var sharedPreferences: SharedPreferences) {

    fun putSession(phoneNumber: String) {
        sharedPreferences.edit().putString(Keys.SHARED_PREFERENCE_KEY, phoneNumber).apply()
    }

    fun getSession(): String? = sharedPreferences.getString(Keys.SHARED_PREFERENCE_KEY, null)

    fun clearSession() {
        sharedPreferences.edit().clear().commit()
    }

}