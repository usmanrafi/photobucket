package com.vend.photobucket.ui.photo.addphoto


import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vend.photobucket.R
import com.vend.photobucket.application.PhotoApplication
import com.vend.photobucket.databinding.AddPhotoDataBinding
import com.vend.photobucket.utils.Keys
import kotlinx.android.synthetic.main.fragment_add_photo.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject


class AddPhotoFragment : Fragment() {
    private val GALLERY = 10
    private val CAMERA = 20
    private val PERMISSIONS = 30

    @Inject
    lateinit var addPhotoViewModel: AddPhotoViewModel

    private lateinit var binding: AddPhotoDataBinding

    var path: String = ""

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


    private fun setupButtons() {
        btnSelectPhoto.setOnClickListener {
            checkForPermissions()
        }


        btnSave.setOnClickListener {
            addPhotoViewModel.validate()
        }
    }

    private fun showSelectionDialog() {
        val pictureDialog = AlertDialog.Builder(this.context!!)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY) {
                data?.let {
                    val contentURI = it.data
                    try {
                        val bitmap = MediaStore.Images.Media
                                .getBitmap(this.context?.contentResolver, contentURI)
                        path = saveImage(bitmap)

                        Toast.makeText(this.context, "Image Saved!", Toast.LENGTH_SHORT).show()
                        btnSelectPhoto!!.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this.context, "Failed!", Toast.LENGTH_SHORT).show()
                    }

                }
            } else if (requestCode == CAMERA) {
                val thumbnail = data?.extras!!.get("data") as Bitmap
                btnSelectPhoto!!.setImageBitmap(thumbnail)
                path = saveImage(thumbnail)
                Toast.makeText(this.context, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveImage(myBitmap: Bitmap): String {


        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + Keys.IMAGE_DIRECTORY_KEY)

        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdirs()

        try {
            val name = "${binding.etImageTitle.text}+${UUID.randomUUID().mostSignificantBits}.jpg"
            val f = File(wallpaperDirectory, name)
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this.context,
                                            arrayOf(f.path),
                                            arrayOf("image/jpeg"), null)
            fo.close()

            addPhotoViewModel.setImageFlag(true)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    private fun additionSuccessful(success: Boolean) {
        if (success) {
            val databaseImageListener = activity as DatabaseImageListener
            addPhotoViewModel.addImage(binding.etImageTitle.text.toString().trim(),
                                       binding.etImageDescription.text.toString().trim(),
                                       path)

            databaseImageListener.imageAdded()

            closeFragment()
        } else
            Toast.makeText(this.context, "Please provide complete information", Toast.LENGTH_SHORT)
                    .show()
    }

    private fun checkForPermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                checkSelfPermission(this.context!!, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(this.context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions()
        } else
            showSelectionDialog()
    }

    private fun requestPermissions() {
        requestPermissions(arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS) {
            if (!grantResults.isEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                showSelectionDialog()
            else
                requestPermissions()
        }
    }

    private fun closeFragment() {
        fragmentManager!!.beginTransaction()
                .remove(this)
                .commit()

        fragmentManager!!.popBackStack()
        addPhotoViewModel.setImageFlag(false)
    }
}
