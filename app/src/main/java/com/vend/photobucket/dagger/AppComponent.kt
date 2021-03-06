package com.vend.photobucket.dagger

import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.ui.authentication.AuthenticationActivity
import com.vend.photobucket.ui.authentication.forgot.ForgotFragment
import com.vend.photobucket.ui.authentication.forgot.NewPasswordFragment
import com.vend.photobucket.ui.authentication.login.LoginFragment
import com.vend.photobucket.ui.authentication.register.RegisterFragment
import com.vend.photobucket.ui.photo.PhotoActivity
import com.vend.photobucket.ui.photo.addphoto.AddPhotoFragment
import com.vend.photobucket.ui.photo.details.DetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class])

interface AppComponent {
    fun inject(application: PhotoApplication)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(forgotFragment: ForgotFragment)
    fun inject(newPasswordFragment: NewPasswordFragment)
    fun inject(homeActivity: PhotoActivity)
    fun inject(authenticationActivity: AuthenticationActivity)
    fun inject(addPhotoFragment: AddPhotoFragment)
    fun inject(detailsFragment: DetailsFragment)
}