package me.ajay.imagegallery.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import me.ajay.imagegallery.databinding.FragmentDetailsBinding

class DetailsFragment : BottomSheetDialogFragment() {

    private val detailsFragmentViewModel: DetailsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailsBinding.bind(view)
        val image = detailsFragmentViewModel.imageItem!!
        binding.apply {
            userName.text = image.user
            imageViews.text = image.views.toString()
            liked.text = image.likes.toString()

            if (0L != image.favorites) {
                favourites.isVisible = true
                favourites.text = image.favorites.toString()
            } else {
                favourites.isVisible = false
            }

            Glide.with(this@DetailsFragment)
                .load(image.userImageURL)
                .circleCrop()
                .transform(CircleCrop())
                .error(android.R.drawable.ic_menu_gallery).into(userProfile)

            Glide.with(this@DetailsFragment)
                .load(image.largeImageURL)
                .error(android.R.drawable.ic_menu_gallery)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }
                }).into(imageView)

            val uri = Uri.parse(image.pageURL)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            webSource.apply {
                text = detailsFragmentViewModel.getHostName(image.pageURL)
                setOnClickListener {
                    detailsFragmentViewModel.onWebSourceClicked()
                }
                paint.isUnderlineText = true
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            detailsFragmentViewModel.detailsEvent.collect { event ->
                when (event) {
                    DetailsFragmentViewModel.ImageDetailEvent.NavigateToWebSource -> {
                        val uri = Uri.parse(image.pageURL)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}