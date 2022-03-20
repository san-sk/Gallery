package com.san.gallery.viewmodel

import androidx.lifecycle.ViewModel
import com.san.gallery.data.ImageFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor() : ViewModel() {

    var albums: List<ImageFolder> = listOf()

}