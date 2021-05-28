package me.ajay.imagegallery.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import me.ajay.imagegallery.data.GalleryImage
import me.ajay.imagegallery.databinding.ImageItemBinding

class GalleryAdapter :
    PagingDataAdapter<GalleryImage, GalleryAdapter.ImageViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = getItem(position)
        if (currentImage != null) {
            holder.bind(currentImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    inner class ImageViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: GalleryImage) {
            binding.apply {
                Glide.with(itemView)
                    .load(image.previewURL)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(CenterCrop(), RoundedCorners(25))
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(imageView)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GalleryImage>() {
        override fun areItemsTheSame(
            oldItem: GalleryImage,
            newItem: GalleryImage
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: GalleryImage,
            newItem: GalleryImage
        ): Boolean = oldItem == newItem

    }
}