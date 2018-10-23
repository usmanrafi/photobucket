package com.vend.photobucket.ui.photo.addphoto


import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.databinding.AddPhotoDataBinding
import com.vend.photobucket.ui.photo.PhotoActivity
import kotlinx.android.synthetic.main.fragment_add_photo.*
import javax.inject.Inject

class AddPhotoFragment : Fragment() {

    @Inject
    lateinit var addPhotoViewModel: AddPhotoViewModel

    private lateinit var binding: AddPhotoDataBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        btnSave.setOnClickListener {
            addPhotoViewModel.validate()
        }
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_photo, container, false)
        binding.setLifecycleOwner { this.lifecycle }

        addPhotoViewModel.resetValidationFlag()
        binding.addphoto = addPhotoViewModel
    }

    private fun subscribe() {
        addPhotoViewModel.getValidationFlag().observe(this, Observer {
            additionSuccessful(it!!)
        })
    }

    private fun additionSuccessful(success: Boolean) {
        if (success) {
            val imagePath = "'"
            val activity = activity as PhotoActivity
            activity.addImage(binding.etImageTitle.text.toString().trim(),
                                                 binding.etImageDescription.text.toString().trim(),
                                                 imagePath)

            activity.onBackPressed()
        }
        else
            Toast.makeText(this.context, "Please provide complete information", Toast.LENGTH_SHORT)
                    .show()
    }
}
