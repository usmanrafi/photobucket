package com.vend.photobucket.ui.authentication.forgot

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
import com.vend.photobucket.databinding.ForgotDataBinding
import kotlinx.android.synthetic.main.fragment_forgot.*
import javax.inject.Inject

class ForgotFragment : Fragment(), ForgotClickListener {


    private val PHONE_NUMBER_HINT = 100
    private val FILL_IN_INTENT = null
    private val FLAGS_MASK = 0
    private val FLAGS_VALUES = 0
    private val EXTRA_FLAGS = 0
    private val OPTION = null


    private lateinit var binding: ForgotDataBinding

    @Inject
    lateinit var forgotViewModel: ForgotViewModel
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

    override fun verifyNumber(view: View) {
        forgotViewModel.checkUser()
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot, container, false)
        binding.setLifecycleOwner(this)

        binding.forgot = forgotViewModel
        binding.clickListener = this
    }


    private fun subscribe() {
        forgotViewModel.getLiveUserExistsFlag().observe(this, Observer {
            userVerified(it!!)
        })
    }

    private fun userVerified(it: Boolean) {
        if (it)
            openNextScreen()
    }

    private fun openNextScreen() {
        fragmentManager?.beginTransaction()
                ?.replace(android.R.id.content, NewPasswordFragment())
                ?.addToBackStack("ForgotFragment")
                ?.commit()
    }

    private fun getPhoneNumber() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()

        try {
            val googleApiClient = GoogleApiClient.Builder(appContext).addApi(Auth.CREDENTIALS_API).build()

            val pendingIntent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
            startIntentSenderForResult(
                    pendingIntent.intentSender,
                    PHONE_NUMBER_HINT,
                    FILL_IN_INTENT, FLAGS_MASK, FLAGS_VALUES, EXTRA_FLAGS, OPTION)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_NUMBER_HINT && resultCode == AppCompatActivity.RESULT_OK) {
            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
            val phoneNumber = credential?.id

            forgotViewModel.setPhoneNumber(phoneNumber!!)
            etPhoneNumber.setText(phoneNumber)
        }
    }
}
