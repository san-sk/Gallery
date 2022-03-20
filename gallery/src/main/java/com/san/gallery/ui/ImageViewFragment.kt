package com.san.gallery.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.san.gallery.R
import com.san.gallery.adapter.ImageViewPagerAdapter
import com.san.gallery.adapter.SmallPreviewAdapter
import com.san.gallery.databinding.FragmentImageViewBinding

class ImageViewFragment : Fragment(R.layout.fragment_image_view) {

    private lateinit var binding: FragmentImageViewBinding

    private val args: ImageViewFragmentArgs by navArgs()

    private val adapter: ImageViewPagerAdapter by lazy {
        ImageViewPagerAdapter()
    }

    private val smallPreviewAdapter by lazy {
        SmallPreviewAdapter(images = args.images?.images ?: listOf()) { pos, item ->
            binding.vpImageview.currentItem = pos
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentImageViewBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        loadImages()
        loadSmallPreview()
    }

    private fun loadImages() {
        binding.vpImageview.adapter = adapter
        adapter.submitList(args.images?.images)
        binding.vpImageview.setCurrentItem(args.position, false)

        binding.vpImageview.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                smallPreviewAdapter.setPosition(position)
                binding.rvSmallPreview.scrollToPosition(position)
            }
        })

    }

    private fun loadSmallPreview() {
        binding.rvSmallPreview.adapter = smallPreviewAdapter
        smallPreviewAdapter.setPosition(args.position)
        binding.rvSmallPreview.scrollToPosition(args.position)
    }

}