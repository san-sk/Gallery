package com.san.gallery.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.san.gallery.R
import com.san.gallery.adapter.ImageViewPagerAdapter
import com.san.gallery.databinding.FragmentImageViewBinding

class ImageViewFragment : Fragment(R.layout.fragment_image_view) {

    private lateinit var binding: FragmentImageViewBinding

    private val args: ImageViewFragmentArgs by navArgs()

    private val adapter: ImageViewPagerAdapter by lazy {
        ImageViewPagerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentImageViewBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.vpImageview.adapter = adapter
        loadImages()
    }

    private fun loadImages() {
        adapter.submitList(args.images?.images)
        binding.vpImageview.setCurrentItem(args.position, false)

    }

}