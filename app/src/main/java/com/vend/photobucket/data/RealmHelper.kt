package com.vend.photobucket.data

import com.vend.photobucket.model.Credentials
import com.vend.photobucket.model.User
import io.realm.Realm

class RealmHelper(var realm: Realm) {


    fun createUser(user: User, credentials: Credentials) {
        realm.executeTransaction({
                                     it.copyToRealm(user)
                                     it.copyToRealm(credentials)
                                 })
    }

    fun getUser(phoneNumber: String) = realm.where(User::class.java).equalTo("phoneNumber", phoneNumber)
            .findFirst()

    fun getCredentials(phoneNumber: String) =
            realm.where(Credentials::class.java).equalTo("phoneNumber", phoneNumber)
                    .findFirst()

    fun updateUserPassword(phoneNumber: String, newPassword: String) {
        val credentials = getCredentials(phoneNumber)
        realm.executeTransaction({
                                     credentials!!.password = newPassword
                                     it.copyToRealmOrUpdate(credentials)
                                 })
    }
}