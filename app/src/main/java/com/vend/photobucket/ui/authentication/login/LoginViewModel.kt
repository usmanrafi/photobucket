package com.vend.photobucket.ui.authentication.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper

class LoginViewModel(var realmHelper: RealmHelper,
                     var sharedPreferenceHelper: SharedPreferenceHelper) : ViewModel() {

    private val validationFlag = MutableLiveData<Boolean>()

    private var phoneNumber: String = ""

    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    private var password: String = ""
    fun getPassword() = password
    fun setPassword(password: String) {
        this.password = password
    }

    fun getValidationFlag() = validationFlag as LiveData<Boolean>

    fun validate() {
        validationFlag.value = validateInfo()
    }

    fun validateInfo(): Boolean {
        phoneNumber = phoneNumber.trim()
        if (phoneNumber.length >= 8 && password.length >= 5) {
            val credentials = realmHelper.getCredentials(phoneNumber)
            if (credentials?.password == password)
                return true
        }
        return false
    }

    fun startSession() {
        sharedPreferenceHelper.putSession(phoneNumber)
    }
}

