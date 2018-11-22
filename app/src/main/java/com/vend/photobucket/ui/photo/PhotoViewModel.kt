package com.vend.photobucket.ui.photo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.model.Image
import com.vend.photobucket.model.User
import java.util.*

class PhotoViewModel(var sharedPreferenceHelper: SharedPreferenceHelper,
                     var realmHelper: RealmHelper)
    : ViewModel() {

    private val user = MutableLiveData<User>()
    private val data = MutableLiveData<ArrayList<Image>>()

    fun getUser() = user as LiveData<User>
    fun getData() = data as LiveData<ArrayList<Image>>

    fun checkSession() {
        val phoneNumber = sharedPreferenceHelper.getSession()
        phoneNumber?.let {
            user.value = realmHelper.getUser(it)

            val list: ArrayList<Image> = realmHelper.getImages(it) as ArrayList
            data.value = list
        }
    }

    fun clearSession() {
        sharedPreferenceHelper.clearSession()
    }

    fun updateImage(image: Image) {
        realmHelper.addImage(image)
        data.value?.set(data.value?.indexOf(image)!!, image)
    }

    fun deleteImages(images: ArrayList<Image>) {
        images.forEach {
            data.value?.remove(it)
            realmHelper.deleteImage(it)
        }
    }

    fun convertImagesToUpperCase(){
        val phoneNumber = sharedPreferenceHelper.getSession()
        phoneNumber?.let {
            realmHelper.convertImageTitlesToUppercase(it)
        }
    }
}