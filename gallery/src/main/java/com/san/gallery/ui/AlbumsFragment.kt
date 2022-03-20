package com.san.gallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.san.gallery.R
import com.san.gallery.adapter.AlbumAdapter
import com.san.gallery.databinding.FragmentAlbumsBinding
import com.san.gallery.utils.setVisibility
import com.san.gallery.viewmodel.CommonViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumsFragment : Fragment(R.layout.fragment_albums) {

    private lateinit var binding: FragmentAlbumsBinding

    private val viewModel by activityViewModels<CommonViewModel>()

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
        if (viewModel.albums.isEmpty()) loadAlbums() else adapter.apply {
            submitList(viewModel.albums)
            saveOriginalList(viewModel.albums)
        }
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.etSearch.apply {
            doAfterTextChanged { text ->
                if (text?.length ?: 0 > 0) adapter.filter.filter(text)
                else adapter.submitList(viewModel.albums)
                binding.tvNotFound.setVisibility(adapter.currentList.isEmpty())
            }
        }
    }

    private fun loadAlbums() {
        lifecycleScope.launch {
            viewModel.albumsFlow.collectLatest {
                it?.let {
                    viewModel.albums = it
                    adapter.submitList(viewModel.albums)
                    adapter.saveOriginalList(viewModel.albums)
                    binding.tvNotFound.setVisibility(adapter.currentList.isEmpty())
                }
            }
            viewModel.loadGallery(requireActivity())
        }
    }
}