package com.vend.photobucket.ui.authentication.register

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
import com.vend.photobucket.databinding.RegisterDataBinding
import com.vend.photobucket.ui.photo.PhotoActivity
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

class RegisterFragment : Fragment(), RegisterClickListener {

    private lateinit var binding: RegisterDataBinding

    @Inject
    lateinit var appContext: Context
    @Inject
    lateinit var registerViewModel: RegisterViewModel

    private val PHONE_NUMBER_HINT = 100

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

    override fun register(view: View) {
        registerViewModel.validate()
    }

    override fun openLoginScreen(view: View) {
        fragmentManager?.popBackStack()
    }

    private fun registerSuccessful(flag: Boolean) {
        if (flag) {
            registerViewModel.registerUser()
            registerViewModel.startSession()

            startActivity(Intent(activity, PhotoActivity::class.java))
            activity!!.finish()
        }
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.setLifecycleOwner(this)

        binding.register = registerViewModel
        binding.clickListener = this
    }

    private fun subscribe() {
        registerViewModel.getValidationFlag().observe(this, Observer {
            registerSuccessful(it!!)
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

            registerViewModel.setPhoneNumber(phoneNumber!!)
            etPhoneNumber.setText(phoneNumber)
        }
    }
}
