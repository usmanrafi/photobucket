package com.vend.photobucket.ui.authentication.forgot

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper

class ForgotViewModel(var realmHelper: RealmHelper,
                      var sharedPreferenceHelper: SharedPreferenceHelper) : ViewModel() {

    private var userExists = MutableLiveData<Boolean>()
    fun getLiveUserExistsFlag() = userExists as LiveData<Boolean>

    private var changeVerified = MutableLiveData<Boolean>()
    fun getLiveChangeVerified() = changeVerified as LiveData<Boolean>

    private var phoneNumber: String = ""
    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    private var newPassword: String = ""
    fun getNewPassword() = newPassword
    fun setNewPassword(newPassword: String) {
        this.newPassword = newPassword
    }

    private var confirmPassword: String = ""
    fun getConfirmPassword() = confirmPassword
    fun setConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
    }

    fun checkUser() {
        val credentials = realmHelper.getCredentials(phoneNumber)
        userExists.value = credentials != null
    }

    fun changePassword() {
        if ((newPassword.length >= 5) &&
                (confirmPassword.length >= 5) &&
                (newPassword == confirmPassword)) {

            realmHelper.updateUserPassword(phoneNumber, newPassword)
            startSession()
            changeVerified.value = true
        } else
            changeVerified.value = false
    }

    private fun startSession() {
        sharedPreferenceHelper.putSession(this.phoneNumber)
    }
}