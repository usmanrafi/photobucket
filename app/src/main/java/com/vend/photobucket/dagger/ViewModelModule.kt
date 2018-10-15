package com.vend.photobucket.dagger

import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.ui.authentication.forgot.ForgotViewModel
import com.vend.photobucket.ui.authentication.login.LoginViewModel
import com.vend.photobucket.ui.authentication.login.LoginViewModelFactory
import com.vend.photobucket.ui.authentication.register.RegisterViewModel
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideLoginViewModelFactory(realmHelper: RealmHelper, sharedPreferenceHelper: SharedPreferenceHelper)
            = LoginViewModelFactory(realmHelper, sharedPreferenceHelper)

    @Provides
    @Singleton
    fun provideLoginViewModel(realmHelper: RealmHelper, sharedPreferenceHelper: SharedPreferenceHelper)
            = LoginViewModel(realmHelper, sharedPreferenceHelper)

    @Provides
    @Singleton
    fun provideRegisterViewModel(realmHelper: RealmHelper, sharedPreferenceHelper: SharedPreferenceHelper)
            = RegisterViewModel(realmHelper, sharedPreferenceHelper)

    @Provides
    @Singleton
    fun provideForgotViewModel(realmHelper: RealmHelper, sharedPreferenceHelper: SharedPreferenceHelper)
            = ForgotViewModel(realmHelper, sharedPreferenceHelper)
}