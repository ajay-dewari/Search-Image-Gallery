package me.ajay.imagegallery.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.ajay.imagegallery.R
import me.ajay.imagegallery.data.GalleryImage
import me.ajay.imagegallery.databinding.FragmentGalleryBinding

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val galleryViewModel: GalleryViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGalleryBinding.bind(view)
        setHasOptionsMenu(true)
        val galleryAdapter = GalleryAdapter()
        binding.apply {
            recyclerView.apply {
                itemAnimator = null
                adapter = galleryAdapter.withLoadStateHeaderAndFooter(
                    header = ImageLoadStateFooterAdapter { galleryAdapter.retry() },
                    footer = ImageLoadStateFooterAdapter { galleryAdapter.retry() }
                )
                val gridColumnCount :Int = requireContext().resources.getInteger(R.integer.grid_view_column_count)
                layoutManager = GridLayoutManager(requireContext(), gridColumnCount)
                setHasFixedSize(true)
            }
            buttonRetry.setOnClickListener {
                galleryViewModel.onRetryClicked()
            }
        }

        galleryAdapter.setOnItemClickListener(object : GalleryAdapter.OnItemClickListener {
            override fun onItemClickListener(image: GalleryImage) {
                galleryViewModel.onRecyclerViewItemClicked(image)
            }
        })

        galleryViewModel.images.observe(viewLifecycleOwner) {
            galleryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            galleryViewModel.galleryEvent.collect { event ->
                when(event) {
                    is GalleryViewModel.GalleryEvent.NavigateToDetails -> {
                        val action =
                            GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(event.image)
                        findNavController().navigate(action)
                    }
                    is GalleryViewModel.GalleryEvent.RetryLoading -> {
                        galleryAdapter.retry()
                    }
                }
            }
        }

        galleryAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    galleryAdapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = galleryViewModel.pendingQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    galleryViewModel.setSearchQuery(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                galleryViewModel.pendingQuery.value = newText
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
    }
}