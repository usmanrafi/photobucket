package com.vend.photobucket.ui.authentication.forgot


import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.databinding.NewPasswordDataBinding
import com.vend.photobucket.ui.photo.PhotoActivity
import javax.inject.Inject

class NewPasswordFragment: Fragment(), NewPasswordClickListener {

    private lateinit var binding: NewPasswordDataBinding

    @Inject
    lateinit var forgotViewModel: ForgotViewModel

    override fun changePassword(view: View) {
        forgotViewModel.changePassword()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.applicationContext as PhotoApplication).getAppComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setupBinding(inflater, container)
        subscribe()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_password, container, false)
        binding.setLifecycleOwner(this)

        binding.forgot = forgotViewModel
        binding.clickListener = this
    }

    private fun subscribe(){
        forgotViewModel.getLiveChangeVerified().observe(this, Observer {
            changeVerified(it!!)
        })
    }

    private fun changeVerified(flag: Boolean){
        if(flag)
            openPhotoActivity()
    }

    private fun openPhotoActivity(){
        startActivity(Intent(activity, PhotoActivity::class.java))
        activity!!.finish()
    }
}
