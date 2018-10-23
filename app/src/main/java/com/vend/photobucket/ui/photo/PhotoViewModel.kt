package com.vend.photobucket.ui.photo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vend.photobucket.data.RealmHelper
import com.vend.photobucket.data.SharedPreferenceHelper
import com.vend.photobucket.model.Image
import com.vend.photobucket.model.User
import io.realm.RealmList
import java.util.*
import kotlin.collections.ArrayList

class PhotoViewModel(var sharedPreferenceHelper: SharedPreferenceHelper,
                     var realmHelper: RealmHelper)
    : ViewModel(){

    private val user = MutableLiveData<User>()
    private val data = MutableLiveData<ArrayList<Image>>()

    fun getUser() = user as LiveData<User>
    fun getData() = data as LiveData<ArrayList<Image>>

    fun checkSession(){
        val phoneNumber = sharedPreferenceHelper.getSession()
        phoneNumber?.let{
            user.value = realmHelper.getUser(it)

            val list: ArrayList<Image> = realmHelper.getImages(it) as ArrayList

//            data.value = if(list.isEmpty()) RealmList() else list
            data.value = list
        }
    }

    fun clearSession() {
        sharedPreferenceHelper.clearSession()
    }

    fun addImage(title: String, description: String, path: String){
        val image = Image(UUID.randomUUID().leastSignificantBits,
                          user.value!!.phoneNumber,
                          title,
                          description,
                          path)

        realmHelper.addImage(image)
        data.value?.add(image)
    }

    fun updateImage(image: Image){
        realmHelper.addImage(image)
        data.value?.set(data.value?.indexOf(image)!!, image)
    }

    fun deleteImages(images: ArrayList<Image>){
        images.forEach{
            data.value?.remove(it)
            realmHelper.deleteImage(it)
        }
    }
}