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
import com.san.gallery.utils.setVisibility


class ImageAdapter(val context: Context, val imageAdapter: ImageAdapterInterface) :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(ImageDiffCallBack()) {

    var isPickerModeEnabled = false
    val selectedItems = mutableListOf<ImageItem>()

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
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun onBind(item: ImageItem) {
            with(binding) {
                binding.cbSelectItem.apply {
                    setVisibility(isPickerModeEnabled)
                    isChecked = selectedItems.contains(item)
                }
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
                    if (!isPickerModeEnabled) imageAdapter.onClick(adapterPosition, item)
                }

                binding.cbSelectItem.setOnClickListener {
                    selectedItems.contains(item).let {
                        binding.cbSelectItem.isChecked = !it
                        if (it) selectedItems.remove(item) else selectedItems.add(item)
                    }
                }

                root.setOnLongClickListener {
                    isPickerModeEnabled = true
                    notifyDataSetChanged()
                    false
                }
            }
        }
    }

    fun interface ImageAdapterInterface {
        fun onClick(position: Int, item: ImageItem)
    }


}