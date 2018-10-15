package com.vend.photobucket.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Image(
        @PrimaryKey
        var title: String = "",
        var description: String = "",
        var encodedImage: String = ""
        ): RealmObject()