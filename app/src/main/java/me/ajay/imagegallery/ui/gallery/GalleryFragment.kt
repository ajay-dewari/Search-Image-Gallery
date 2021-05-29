package me.ajay.imagegallery.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.ajay.imagegallery.R
import me.ajay.imagegallery.data.GalleryImage
import me.ajay.imagegallery.databinding.FragmentGalleryBinding
import me.ajay.imagegallery.util.onQueryTextSubmit

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val galleryViewModel : GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGalleryBinding.bind(view)
        setHasOptionsMenu(true)
        val imageAdapter = GalleryAdapter()
        binding.apply {
            recyclerView.apply {
                itemAnimator = null
                adapter = imageAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
            }
            buttonRetry.setOnClickListener {
                galleryViewModel.onRetryClicked()
            }
        }

        imageAdapter.setOnItemClickListener(object : GalleryAdapter.OnItemClickListener {
            override fun onItemClickListener(image: GalleryImage) {
                galleryViewModel.onRecyclerViewItemClicked(image)
            }
        })

        galleryViewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            galleryViewModel.galleryEvent.collect { event ->
                when(event) {
                    is GalleryViewModel.GalleryEvent.NavigateToDetails -> {
                        //navigate to detail screen
                    }
                    is GalleryViewModel.GalleryEvent.RetryLoading -> {
                        imageAdapter.retry()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextSubmit { query ->
            if (query.isNotBlank()) {
                galleryViewModel.setSearchQuery(query)
                searchView.clearFocus()
            }
        }
    }
}