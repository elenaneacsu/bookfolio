package com.elenaneacsu.bookfolio.ui.search.book_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentBookDetailsBinding
import com.elenaneacsu.bookfolio.extensions.Result
import com.elenaneacsu.bookfolio.extensions.createBottomSheetDialog
import com.elenaneacsu.bookfolio.extensions.toast
import com.elenaneacsu.bookfolio.extensions.updateStatusBarColor
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.shelves.shelf.ShelfFragmentArgs
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 01/06/21
 */
@AndroidEntryPoint
class BookDetailsFragment : ShelfOptionsAdapter.OnItemClickListener,
    BaseMvvmFragment<BookDetailsViewModel, FragmentBookDetailsBinding>(
    R.layout.fragment_book_details, BookDetailsViewModel::class.java
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.apply {
            manageBottomNavigationVisibility(View.GONE)
            updateStatusBarColor(R.color.accent, false)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return

        val args = BookDetailsFragmentArgs.fromBundle(bundle)
        viewBinding.book = args.book
    }

    override fun initViews() {
        super.initViews()

        viewBinding.fabShelves.setOnOneOffClickListener {
            viewModel.getShelves()
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.shelvesResult.observe(viewLifecycleOwner, {
            when(it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { shelves -> showShelvesDialog(shelves) }
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }

    override fun hideProgress() {
        viewBinding.progressArea.visibility = View.GONE
    }

    override fun showProgress() {
        viewBinding.progressArea.visibility = View.VISIBLE
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    override fun onShelfOptionClicked(shelf: Shelf) {
        shelf.name?.let { toast(it, Toast.LENGTH_LONG) }
    }

    private fun showShelvesDialog(shelves: List<Shelf>) {
        val sheetView = requireActivity().layoutInflater.inflate(R.layout.bottom_sheet_options_dialog, null)
        val bottomSheetDialog = createBottomSheetDialog(sheetView, "Add this book into shelf")

        val adapter = ShelfOptionsAdapter(requireContext(), this)
        adapter.add(shelves)

        val recyclerView = sheetView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        bottomSheetDialog?.show()
    }
}