package com.san.gallery.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.san.gallery.R
import com.san.gallery.adapter.ImageAdapter
import com.san.gallery.databinding.FragmentImagesBinding

class ImagesFragment : Fragment(R.layout.fragment_images) {

    private lateinit var binding: FragmentImagesBinding

    private val args: ImagesFragmentArgs by navArgs()

    private val adapter: ImageAdapter by lazy {
        ImageAdapter(requireContext()) {
            findNavController().navigate(
                ImagesFragmentDirections.actionImagesFragmentToImageViewFragment(
                     images = args.images, position =  it
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentImagesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.rvImages.adapter = adapter
        loadAlbums()
    }

    private fun loadAlbums() {
        adapter.submitList(args.images?.images)
    }

}