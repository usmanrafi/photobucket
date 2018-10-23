package com.vend.photobucket.ui.photo.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vend.photobucket.R
import com.vend.photobucket.model.Image
import com.vend.photobucket.ui.photo.PhotoActivity
import kotlinx.android.synthetic.main.fragment_details.*

private const val ARG_IMAGE = "image"

class DetailsFragment : Fragment() {

    private var editable = false

    private lateinit var image: Image

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_details, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupButtons()
    }

    private fun setupView() {
        //todo: show Image

        etTitle.setText(image.title)
        etDescription.setText(image.description)
        enableEditing(editable)
    }

    private fun enableEditing(enable: Boolean) {
        if (enable) {
            btnEdit.setImageResource(R.drawable.ic_cancel_black_24dp)

            etTitle.inputType = InputType.TYPE_CLASS_TEXT

            etDescription.inputType = InputType.TYPE_CLASS_TEXT

            btnShare.visibility = View.GONE
            btnSave.visibility = View.VISIBLE

        } else {
            btnEdit.setImageResource(R.drawable.ic_edit_black_24dp)

            etTitle.setText(image.title)
            etTitle.inputType = InputType.TYPE_NULL

            etDescription.setText(image.description)
            etDescription.inputType = InputType.TYPE_NULL

            btnSave.visibility = View.GONE
            btnShare.visibility = View.VISIBLE
        }

        editable = enable
    }

    private fun setupButtons() {
        btnEdit.setOnClickListener {
            enableEditing(!editable)
        }

        btnShare.setOnClickListener {
            //todo: Share
        }

        btnSave.setOnClickListener{
            image.title = etTitle.text.toString().trim()
            image.description = etDescription.text.toString().trim()

            enableEditing(false)
            // todo: persist

            (activity as PhotoActivity).updateItem(image)
            activity?.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param = it.getString(ARG_IMAGE)
            val arr = param.split("|")
            image = Image(arr[0].toLong(), arr[1], arr[2], arr[3], arr[4])
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(image: Image) =
                DetailsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_IMAGE, image.toString())
                    }
                }
    }

}