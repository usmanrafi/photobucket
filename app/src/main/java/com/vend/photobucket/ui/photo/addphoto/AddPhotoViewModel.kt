package com.vend.photobucket.ui.photo.addphoto

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class AddPhotoViewModel : ViewModel() {

    private var title: String = ""
    private var description: String = ""
    private var imageFlag: Boolean = false

    private var validationFlag = MutableLiveData<Boolean>()

    fun getTitle() = title
    fun setTitle(title: String) {
        this.title = title.trim()
    }

    fun getDescription() = description
    fun setDescription(description: String) {
        this.description = description.trim()
    }

    fun setImageFlag(flag: Boolean) {
        imageFlag = flag
    }

    fun resetValidationFlag() {
        validationFlag.value = false
    }

    fun getValidationFlag() = validationFlag as LiveData<Boolean>

    fun validate() {
        validationFlag.value = validateDetails()
    }

    private fun validateDetails(): Boolean =
            (imageFlag && title.isNotBlank() && description.isNotBlank())
}