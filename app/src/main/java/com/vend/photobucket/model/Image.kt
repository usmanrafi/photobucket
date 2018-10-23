package com.vend.photobucket.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Image(
        @PrimaryKey
        var title: String = "",
        var description: String = "",
        var path: String = ""
        ): RealmObject() {

    override fun equals(other: Any?): Boolean {
        if(other is Image){
            return other.title == this.title
        }
        return super.equals(other)
    }
}