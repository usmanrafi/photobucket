package com.vend.photobucket.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.ui.authentication.login.LoginFragment
import com.vend.photobucket.ui.photo.PhotoActivity
import javax.inject.Inject

class AuthenticationActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Inject
    lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as PhotoApplication).getAppComponent().inject(this)


        if (sharedPreferenceHelper.getSession() != null)
            openPhotoActivity()
        else
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, loginFragment)
                    .commit()
    }

    private fun openPhotoActivity() {
        startActivity(Intent(this, PhotoActivity::class.java))
        finish()
    }
}
