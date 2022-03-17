package com.san.gallery.viewmodel

import androidx.lifecycle.ViewModel
import com.san.gallery.data.ImageFolder
import javax.inject.Inject

class AlbumsViewModel @Inject constructor() : ViewModel() {

    var albums: List<ImageFolder> = listOf()

}