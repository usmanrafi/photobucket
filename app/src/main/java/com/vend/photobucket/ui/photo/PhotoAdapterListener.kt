package com.vend.photobucket.ui.photo

import com.vend.photobucket.model.Image

interface PhotoAdapterListener{
    fun showImageDetails(image: Image)
    fun deleteImages(images: ArrayList<Image>)
    fun getFilteredImages(constraint: String, images: ArrayList<Image>): ArrayList<Image>
}