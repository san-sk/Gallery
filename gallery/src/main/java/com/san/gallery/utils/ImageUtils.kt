package com.san.gallery.utils

import android.media.Image
import com.san.gallery.data.ImageFolder
import com.san.gallery.data.ImageItem


object ImageUtils {

    fun singleListFromImage(image: Image): ArrayList<Image> {
        val images = arrayListOf<Image>()
        images.add(image)
        return images
    }

    fun folderListFromImages(images: List<ImageItem>): List<ImageFolder> {
        val folderMap: MutableMap<Long, ImageFolder> = LinkedHashMap()
        for (image in images) {
            val bucketId = image.bucketId ?: 0L
            val bucketName = image.bucketName
            var folder = folderMap[bucketId]
            if (folder == null) {
                folder = ImageFolder(bucketId, bucketName)
                folderMap[bucketId] = folder
            }
            folder.images.add(image)
        }
        return ArrayList(folderMap.values)
    }

    fun filterImages(images: ArrayList<ImageItem>, bucketId: Long?): ArrayList<ImageItem> {
        if (bucketId == null || bucketId == 0L) return images

        val filteredImages = arrayListOf<ImageItem>()
        for (image in images) {
            if (image.bucketId == bucketId) {
                filteredImages.add(image)
            }
        }
        return filteredImages
    }

}
