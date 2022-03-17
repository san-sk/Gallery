package com.san.gallery.data


import android.content.ContentUris
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.san.gallery.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ImageDataSource(private val activity: FragmentActivity) :
    LoaderManager.LoaderCallbacks<Cursor> {

    private var loadedListener: OnImagesLoadedListener? = null
    private val imageFolders = ArrayList<ImageFolder>()
    private val imageFolderIds = ArrayList<Long>()
    private var currentMode: Int? = null


    /**
     * @param path
     * @param onImagesLoadedListener
     */
    @Suppress("SameParameterValue")
    suspend fun loadImage(onImagesLoadedListener: OnImagesLoadedListener) =
        withContext(Dispatchers.Main) { //loaderManager.initLoader should be run on main thread
            loadedListener = onImagesLoadedListener
            destroyLoader()

            val loaderManager = LoaderManager.getInstance(activity)
            val bundle = Bundle()
            loaderManager.initLoader(LOADER_ALL, bundle, this@ImageDataSource)
        }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        return CursorLoader(activity, collection, PROJECTION, null, null, PROJECTION[6] + " DESC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        imageFolders.clear()
        try {
            if (data != null && data.count > 0) {
                val allImages = ArrayList<ImageItem>()
                while (data.moveToNext()) {

                    val id = data.getLong(data.getColumnIndexOrThrow(PROJECTION[0]))
                    val imageSize = data.getLong(data.getColumnIndexOrThrow(PROJECTION[2]))
                    val imageWidth = data.getInt(data.getColumnIndexOrThrow(PROJECTION[3]))
                    val imageHeight = data.getInt(data.getColumnIndexOrThrow(PROJECTION[4]))
                    val imageMimeType =
                        data.getString(data.getColumnIndexOrThrow(PROJECTION[5]))
                    val imageAddTime = data.getLong(data.getColumnIndexOrThrow(PROJECTION[6]))
                    val bucketId = data.getLong(data.getColumnIndexOrThrow(PROJECTION[7]))
                    val bucketName = data.getString(data.getColumnIndexOrThrow(PROJECTION[8]))

                    val relativePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH))
                    } else {
                        null
                    }
                    val contentUri =
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )

                    val imageItem =
                        ImageItem(bucketId, bucketName, contentUri.toString(), relativePath)
                    imageItem.size = imageSize
                    imageItem.addTime = imageAddTime
                    imageItem.height = imageHeight
                    imageItem.width = imageWidth
                    imageItem.mimeType = imageMimeType
                    allImages.add(imageItem)

                }

                imageFolders.addAll(ImageUtils.folderListFromImages(allImages))
                if (data.count > 0 && allImages.size > 0) {

                    val allImagesFolder =
                        ImageFolder(0L, "All")
                    allImagesFolder.cover = allImages[0]
                    allImagesFolder.images = allImages
                    imageFolders.add(0, allImagesFolder)
                }
            }
        } catch (e: Exception) {
            Log.e("test", e.toString())
        }
        loadedListener?.onImagesLoaded(imageFolders)

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        println("--------")

    }

    private fun destroyLoader() {
        currentMode?.let {
            LoaderManager.getInstance(activity).destroyLoader(it)
        }
    }


    interface OnImagesLoadedListener {
        fun onImagesLoaded(imageFolders: List<ImageFolder>)
    }

    companion object {

        const val LOADER_ALL = 0

        private val PROJECTION: Array<String>
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                projection.plus(MediaStore.Images.Media.RELATIVE_PATH)
            } else projection

        private val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE, //long 132492
            MediaStore.Images.Media.WIDTH, // 1920
            MediaStore.Images.Media.HEIGHT, //  1080
            MediaStore.Images.Media.MIME_TYPE, //    image/jpeg
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
    }


}