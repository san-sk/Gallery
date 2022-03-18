package com.san.gallery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.gallery.R
import com.san.gallery.data.ImageFolder
import com.san.gallery.databinding.ItemAlbumBinding


class AlbumAdapter(val context: Context, val albumAdapter: AlbumAdapter) :
    ListAdapter<ImageFolder, RecyclerView.ViewHolder>(AlbumDiffCallBack()), Filterable {

    private var imageListSrc = listOf<ImageFolder>()

    class AlbumDiffCallBack : DiffUtil.ItemCallback<ImageFolder>() {
        override fun areItemsTheSame(
            oldItem: ImageFolder,
            newItem: ImageFolder,
        ) = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: ImageFolder,
            newItem: ImageFolder,
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlbumItem(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AlbumItem).onBind(getItem(position))
    }

    inner class AlbumItem(var binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: ImageFolder) {
            with(binding) {
                item.name?.let {
                    tvAlbumName.text = "$it(${item.images.size})"
                } ?: run {
                    tvAlbumName.text = "Others"
                }

                try {
                    item.images[0].uri?.let {
                        Glide.with(ivAlbumCover.context).load(it)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivAlbumCover)
                    }
                } catch (e: Exception) {
                    Log.e(
                        this@AlbumAdapter.javaClass.toString(),
                        "Cannot load Glide image in GalleryAdapter $e"
                    )
                }

                root.setOnClickListener {
                    albumAdapter.onClick(item)
                }
            }
        }
    }

    fun interface AlbumAdapter {
        fun onClick(item: ImageFolder)
    }

    fun saveOriginalList(list: List<ImageFolder>) {
        imageListSrc = list
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = this@AlbumAdapter.imageListSrc.filter {
                        it.name?.contains(p0.toString(), true) == true
                    }

                }
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                (p1?.values as List<ImageFolder>?)?.let {
                    submitList(it)
                }
            }

        }
    }
}