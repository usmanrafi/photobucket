package com.vend.photobucket.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Credentials(
        @PrimaryKey
        var phoneNumber: String = "",
        var password: String = ""
    ): RealmObject()