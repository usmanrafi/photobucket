package com.vend.photobucket.ui.authentication.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.databinding.LoginDataBinding
import com.vend.photobucket.ui.authentication.forgot.ForgotFragment
import com.vend.photobucket.ui.authentication.register.RegisterFragment
import com.vend.photobucket.ui.photo.PhotoActivity
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(), LoginClickListener {

    private val PHONE_NUMBER_HINT = 100

    private lateinit var binding: LoginDataBinding

    @Inject
    lateinit var loginViewModel: LoginViewModel
    @Inject
    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.applicationContext as PhotoApplication).getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setupBinding(inflater, container)
        subscribe()
        getPhoneNumber()

        return binding.root
    }

    override fun login(view: View) {
        loginViewModel.validate()
    }

    override fun openRegisterScreen(view: View) {
        fragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, RegisterFragment())
                ?.addToBackStack("LoginFragment")
                ?.commit()
    }

    override fun openForgotScreen(view: View) {
        fragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, ForgotFragment())
                ?.addToBackStack("LoginFragment")
                ?.commit()
    }


    private fun loginSuccessful(flag: Boolean) {
        if (flag) {
            loginViewModel.startSession()

            startActivity(Intent(appContext, PhotoActivity::class.java))
            activity!!.finish()
        }
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_login, container, false)
        binding.setLifecycleOwner { this.lifecycle }

        binding.login = loginViewModel
        binding.clickListener = this
    }

    private fun subscribe() {
        loginViewModel.getValidationFlag().observe(this, Observer {
            loginSuccessful(it!!)
        })
    }

    private fun getPhoneNumber() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()

        try {
            val googleApiClient = GoogleApiClient.Builder(appContext).addApi(Auth.CREDENTIALS_API).build()

            val pendingIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
            startIntentSenderForResult(
                    pendingIntent.intentSender,
                    PHONE_NUMBER_HINT,
                    null, 0, 0, 0, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_NUMBER_HINT && resultCode == AppCompatActivity.RESULT_OK) {
            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
            val phoneNumber = credential?.id

            loginViewModel.setPhoneNumber(phoneNumber!!)
            etPhoneNumber.setText(phoneNumber)
        }
    }
}
