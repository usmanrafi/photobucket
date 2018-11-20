package com.vend.photobucket.ui.photo.addphoto

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.model.Image
import com.vend.photobucket.model.User
import java.util.*

class AddPhotoViewModel(var sharedPreferenceHelper: SharedPreferenceHelper,
                        var realmHelper: RealmHelper) : ViewModel() {

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

    fun addImage(title: String, description: String, path: String) {

        var user: User? = null

        val phoneNumber = sharedPreferenceHelper.getSession()
        phoneNumber?.let {
            user = realmHelper.getUser(it)
        }

        val image = Image(UUID.randomUUID().leastSignificantBits,
                          user!!.phoneNumber,
                          title,
                          description,
                          path,
                          System.currentTimeMillis())

        realmHelper.addImage(image)
    }

    private fun validateDetails(): Boolean =
            (imageFlag && title.isNotBlank() && description.isNotBlank())
}