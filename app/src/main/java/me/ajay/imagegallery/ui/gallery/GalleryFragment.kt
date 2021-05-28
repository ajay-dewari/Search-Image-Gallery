package me.ajay.imagegallery.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.ajay.imagegallery.R
import me.ajay.imagegallery.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGalleryBinding.bind(view)
    }
}