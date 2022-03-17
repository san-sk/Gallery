package com.san.gallery.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.gallery.R
import com.san.gallery.data.ImageItem
import com.san.gallery.databinding.ItemImageViewBinding


class ImageViewPagerAdapter() :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(ImageDiffCallBack()) {

    class ImageDiffCallBack : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(
            oldItem: ImageItem,
            newItem: ImageItem,
        ) = oldItem.bucketId == newItem.bucketId


        override fun areContentsTheSame(
            oldItem: ImageItem,
            newItem: ImageItem,
        ) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImagesItem(
            ItemImageViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImagesItem).onBind(getItem(position))
    }

    inner class ImagesItem(var binding: ItemImageViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ImageItem) {
            with(binding) {
                try {
                    item.uri?.let {
                        Glide.with(ivFullView.context).load(it)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivFullView)
                    }
                } catch (e: Exception) {
                    Log.e(
                        this@ImageViewPagerAdapter.javaClass.toString(),
                        "Cannot load Glide image in imageview $e"
                    )
                }
            }
        }
    }

}