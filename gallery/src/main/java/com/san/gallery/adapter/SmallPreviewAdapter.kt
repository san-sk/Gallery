package com.san.gallery.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.gallery.R
import com.san.gallery.data.ImageItem
import com.san.gallery.databinding.ItemSmallPreviewBinding
import com.san.gallery.utils.setVisibility


class SmallPreviewAdapter(
    var images: List<ImageItem> = ArrayList(),
    val onItemClickListener: (Int, ImageItem) -> Unit,
) : RecyclerView.Adapter<SmallPreviewAdapter.SmallPreviewViewHolder>() {

    var selectedPosition = 0

    @SuppressLint("NotifyDataSetChanged")
    fun setPosition(pos: Int) {
        selectedPosition = pos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPreviewViewHolder {
        return SmallPreviewViewHolder(
            ItemSmallPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SmallPreviewViewHolder, position: Int) {
        holder.binding.apply {
            root.setOnClickListener {
                onItemClickListener(position, images[position])
            }
            images[position].uri?.let {
                Glide.with(ivSmall.context).load(it)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(ivSmall)
            }
            vFrame.setVisibility(position == selectedPosition)
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = images.size

    inner class SmallPreviewViewHolder(var binding: ItemSmallPreviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}


