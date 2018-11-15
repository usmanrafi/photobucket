package com.vend.photobucket.ui.photo

import com.vend.photobucket.model.Image

interface AdapterListener{
    fun showImageDetails(image: Image)
    fun deleteImages(images: ArrayList<Image>)

}