package com.vend.photobucket.ui.authentication.login

import android.view.View

interface LoginClickListener{
    fun login(view: View)
    fun openRegisterScreen(view: View)
    fun openForgotScreen(view: View)
}