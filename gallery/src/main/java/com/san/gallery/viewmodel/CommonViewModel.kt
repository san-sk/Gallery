package com.san.gallery.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.gallery.data.ImageDataSource
import com.san.gallery.data.ImageFolder
import com.san.gallery.data.ImageItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {

    val albumsFlow = MutableStateFlow<List<ImageFolder>?>(null)
    var albums: List<ImageFolder> = listOf()

    var images: List<ImageItem> = listOf()

    suspend fun collectImages() {
        albumsFlow.collectLatest {
            it?.let {
                albums = it
            }
        }
    }

    suspend fun loadGallery(activity: FragmentActivity) {
        val dataSource = ImageDataSource(activity)

        dataSource.loadImage(object : ImageDataSource.OnImagesLoadedListener {
            override fun onImagesLoaded(imageFolders: List<ImageFolder>) {
                if (imageFolders.isNotEmpty()) {
                    viewModelScope.launch { albumsFlow.emit(imageFolders) }
                }
            }
        })
    }
}