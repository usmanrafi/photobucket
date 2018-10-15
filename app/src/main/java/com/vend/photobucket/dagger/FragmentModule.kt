package com.vend.photobucket.dagger

import com.vend.photobucket.ui.authentication.forgot.ForgotFragment
import com.vend.photobucket.ui.authentication.forgot.NewPasswordFragment
import com.vend.photobucket.ui.authentication.login.LoginFragment
import com.vend.photobucket.ui.authentication.register.RegisterFragment
import com.vend.photobucket.ui.photo.AddPhotoFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FragmentModule {
    @Provides
    @Singleton
    fun provideLoginFragment() = LoginFragment()

    @Provides
    @Singleton
    fun provideRegisterFragment() = RegisterFragment()

    @Provides
    @Singleton
    fun provideForgotFragment() = ForgotFragment()

    @Provides
    @Singleton
    fun provideNewPasswordFragment() = NewPasswordFragment()

    @Provides
    @Singleton
    fun provideAddPhotoFragment() = AddPhotoFragment()
}