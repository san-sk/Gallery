package com.san.gallery.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.gallery.R
import com.san.gallery.data.ImageItem
import com.san.gallery.databinding.ItemImageBinding


class ImageAdapter(val context: Context, val imageAdapter: ImageAdapter) :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(ImageDiffCallBack()) {

    class ImageDiffCallBack : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(
            oldItem: ImageItem,
            newItem: ImageItem,
        ) = oldItem.uri == newItem.uri


        override fun areContentsTheSame(
            oldItem: ImageItem,
            newItem: ImageItem,
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImagesItem(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImagesItem).onBind(getItem(position))
    }

    inner class ImagesItem(var binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(item: ImageItem) {
            with(binding) {

                try {
                    item.uri?.let {
                        Glide.with(ivImageCover.context).load(it)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivImageCover)
                    }
                } catch (e: Exception) {
                    Log.e(
                        this@ImageAdapter.javaClass.toString(),
                        "Cannot load Glide image in GalleryAdapter $e"
                    )
                }

                root.setOnClickListener {
                    imageAdapter.onClick(adapterPosition)
                }
            }
        }
    }

    fun interface ImageAdapter {
        fun onClick(position: Int)
    }
}