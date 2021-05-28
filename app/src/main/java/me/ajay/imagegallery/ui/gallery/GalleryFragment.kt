package me.ajay.imagegallery.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.ajay.imagegallery.R
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
                //Event channel call back needed from viewModel
                imageAdapter.retry()
            }
        }

        galleryViewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.submitData(viewLifecycleOwner.lifecycle, it)
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