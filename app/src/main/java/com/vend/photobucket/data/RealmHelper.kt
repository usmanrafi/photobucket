package com.vend.photobucket.data

import android.os.Build
import android.util.Log
import com.vend.photobucket.model.Credentials
import com.vend.photobucket.model.Image
import com.vend.photobucket.model.User
import io.realm.Realm

class RealmHelper(var realm: Realm) {

    fun createUser(user: User, credentials: Credentials) {
        realm.executeTransaction {
            it.copyToRealm(user)
            it.copyToRealm(credentials)
        }
    }

    fun getUser(phoneNumber: String) = realm.where(User::class.java).equalTo("phoneNumber", phoneNumber)
            .findFirst()

    fun getCredentials(phoneNumber: String) =
            realm.where(Credentials::class.java).equalTo("phoneNumber", phoneNumber)
                    .findFirst()

    fun updateUserPassword(phoneNumber: String, newPassword: String) {
        val credentials = getCredentials(phoneNumber)
        realm.executeTransaction {
            credentials!!.password = newPassword
            it.copyToRealmOrUpdate(credentials)
        }
    }

    fun addImage(image: Image) {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(image)
        }
    }

    fun deleteImage(image: Image) {
        realm.executeTransaction {
            it.where(Image::class.java).equalTo("id", image.id).findFirst()?.deleteFromRealm()
        }
    }

    fun getImages(phoneNumber: String): List<Image> {
        return ArrayList(realm.where(Image::class.java).equalTo("phoneNumber", phoneNumber).findAll())
    }

    fun getImageTitle(image: Image): String{
        return realm.where(Image::class.java).equalTo("id", image.id).findFirst()?.title.toString()
    }

    fun convertImageTitlesToUppercase(phoneNumber: String) {
        val arrayList = getImages(phoneNumber) as ArrayList<Image>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StreamUsage.convertImageTitlesToUppercase(realm, arrayList)
        } else {
            realm.executeTransaction {
                arrayList.forEach {
                    it.title = it.title.toUpperCase()
                    addImage(it)
                    Log.i("Foo", "Normal")
                }
            }
        }
    }
}