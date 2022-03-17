package com.san.gallery.data

import java.io.Serializable
import java.util.ArrayList


data class ImageFolder(
    var id: Long?,
    var name: String?,
    var cover: ImageItem? = null,
    var images: ArrayList<ImageItem> = ArrayList()
) : Serializable