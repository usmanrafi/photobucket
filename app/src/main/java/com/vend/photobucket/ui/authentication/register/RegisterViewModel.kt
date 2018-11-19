package com.vend.photobucket.ui.authentication.register

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.model.Credentials
import com.vend.photobucket.model.User
import com.vend.photobucket.utils.Validation

class RegisterViewModel(var realmHelper: RealmHelper,
                        var sharedPreferenceHelper: SharedPreferenceHelper) : ViewModel() {

    private val validationFlag = MutableLiveData<Boolean>()

    private var firstName = ""
    private var lastName = ""
    private var phoneNumber = ""
    private var password = ""
    private var confirmPassword = ""

    fun getValidationFlag() = validationFlag as LiveData<Boolean>

    fun getFirstName() = firstName
    fun setFirstName(firstName: String) {
        this.firstName = firstName

    }

    fun getLastName() = lastName
    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun getPhoneNumber() = phoneNumber
    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun getPassword() = password
    fun setPassword(password: String) {
        this.password = password
    }

    fun getConfirmPassword() = confirmPassword
    fun setConfirmPassword(confirmPassword: String) {
        this.confirmPassword = confirmPassword
    }

    fun validate() {
        validationFlag.value = validateInfo()
    }

    private fun validateInfo(): Boolean {
        if ((firstName.length >= Validation.NAME_LENGTH) &&
                (lastName.length >= Validation.NAME_LENGTH) &&
                (phoneNumber.length >= Validation.PHONE_NUMBER_LENGTH) &&
                (password.length >= Validation.PASSWORD_LENGTH) &&
                (password == confirmPassword))
            return true

        return false
    }

    fun registerUser() {

        val user = User(this.phoneNumber,
                        this.firstName,
                        this.lastName)

        val credentials = Credentials(this.phoneNumber,
                                      this.password)

        realmHelper.createUser(user, credentials)
    }

    fun startSession() {
        sharedPreferenceHelper.putSession(this.phoneNumber)
    }
}