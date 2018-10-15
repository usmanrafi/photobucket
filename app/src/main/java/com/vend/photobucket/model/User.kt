package com.vend.photobucket.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
            @PrimaryKey
            var phoneNumber: String = "",
            var firstName: String = "",
            var lastName: String = "",
            var images: RealmList<Image> = RealmList()
    )
    : RealmObject(){

    fun getImage(title: String) = images.find {it.title == title}
}