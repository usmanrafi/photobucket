package com.vend.photobucket.ui.authentication.login

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper

class LoginViewModelFactory(var realmHelper: RealmHelper,
                            var sharedPreferenceHelper: SharedPreferenceHelper) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = LoginViewModel(realmHelper, sharedPreferenceHelper) as T
}