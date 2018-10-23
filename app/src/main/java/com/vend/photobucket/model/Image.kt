package com.vend.photobucket.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Image(
        @PrimaryKey
        var id: Long = 0,
        var phoneNumber: String = "",
        var title: String = "",
        var description: String = "",
        var path: String = ""
        ): RealmObject() {

    override fun equals(other: Any?): Boolean {
        if(other is Image){
            return other.id == this.id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(id, phoneNumber, title, description, path)
    }

    override fun toString(): String {
        return "$id|$phoneNumber|$title|$description|$path"
    }
}