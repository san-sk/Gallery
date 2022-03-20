package com.san.gallery

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.san.gallery.ui.AlbumsFragmentDirections
import com.san.gallery.viewmodel.CommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private val viewModel by viewModels<CommonViewModel>()

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                lifecycleScope.launch {
                    viewModel.loadGallery(this@GalleryActivity)
                }
                Log.i("DEBUG", "permission granted")
            } else {
                Log.i("DEBUG", "permission denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        Toast.makeText(this, "galleractivity", Toast.LENGTH_SHORT).show()
    }


}