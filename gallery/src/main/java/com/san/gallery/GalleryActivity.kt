package com.san.gallery

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.san.gallery.ui.AlbumsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Do something if permission granted
            if (isGranted) {
                /*findNavController(R.id.nav_host_fragment).navigate(
                    AlbumsFragmentDirections.actionAlbumFragmentToImagesFragment(
                        null
                    )
                )*/
                Log.i("DEBUG", "permission granted")
            } else {
                Log.i("DEBUG", "permission denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

         */
        setContentView(R.layout.activity_gallery)

        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        Toast.makeText(this, "galleractivity", Toast.LENGTH_SHORT).show()
    }
}