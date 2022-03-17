package com.san.gallery.data


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItem(
    var bucketId: Long?,
    var bucketName: String?,
    var uri: String? = null, //passing Uri as String
    var path: String?,
    var name: String? = null,
    var size: Long = 0,
    var width: Int = 0,
    var height: Int = 0,
    var mimeType: String? = null,
    var addTime: Long = 0
) : Parcelable
