package com.san.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.san.gallery.R
import com.san.gallery.adapter.AlbumAdapter
import com.san.gallery.data.ImageDataSource
import com.san.gallery.data.ImageFolder
import com.san.gallery.databinding.FragmentAlbumsBinding
import com.san.gallery.viewmodel.AlbumsViewModel
import kotlinx.coroutines.launch

class AlbumsFragment : Fragment(R.layout.fragment_albums) {

    private lateinit var binding: FragmentAlbumsBinding

    private val imageDataSource by lazy { ImageDataSource(requireActivity()) }

    private val viewModel by viewModels<AlbumsViewModel>()

    private val adapter: AlbumAdapter by lazy {
        AlbumAdapter(requireContext()) {
            findNavController().navigate(
                AlbumsFragmentDirections.actionAlbumFragmentToImagesFragment(
                    it
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAlbums.adapter = adapter
        if (viewModel.albums.isEmpty()) loadAlbums() else adapter.submitList(viewModel.albums)
        Toast.makeText(requireContext(),"album fragment", Toast.LENGTH_SHORT).show()
    }

    private fun loadAlbums() {
        lifecycleScope.launch {
            imageDataSource.loadImage(object : ImageDataSource.OnImagesLoadedListener {
                override fun onImagesLoaded(imageFolders: List<ImageFolder>) {
                    if (imageFolders.isNotEmpty()) {
                        viewModel.albums = imageFolders
                        adapter.submitList(imageFolders)
                    }
                }
            })
        }
    }
}