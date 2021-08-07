package com.elenaneacsu.bookfolio.ui.search.book_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentBookDetailsBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.models.UserBook
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.utils.date.toStringDate
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 01/06/21
 */
@AndroidEntryPoint
class BookDetailsFragment : ShelfOptionsAdapter.OnItemClickListener,
    BaseMvvmFragment<BookDetailsViewModel, FragmentBookDetailsBinding>(
        R.layout.fragment_book_details, BookDetailsViewModel::class.java
    ) {

    private var book: Item? = null
    private var userSavedBook: UserBook? = null
    private var bookDetailsMapper: BookDetailsMapper? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

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

        book = Item(args.book?.id, args.book?.volumeInfo)
        userSavedBook = args.userSavedBook

        bookDetailsMapper = BookDetailsMapper(args.userSavedBook, args.book)
        viewBinding.book = bookDetailsMapper
    }

    override fun initViews() {
        super.initViews()

        viewBinding.apply {
            fabShelves.setOnOneOffClickListener {
                viewModel.getShelves()
            }

            startDateIcon.setOnOneOffClickListener {
                showMaterialDatePicker(false, {
                    it.dismiss()
                }, {
                    val formattedDate = it.toStringDate()
                    startDate.text = formattedDate
                })
            }

            endDateIcon.setOnOneOffClickListener {
                showMaterialDatePicker(false, { it.dismiss() }, {
                    endDate.text = it.toStringDate()
                })
            }
        }

    }

    override fun initObservers() {
        super.initObservers()

        viewModel.shelvesResult.observe(viewLifecycleOwner, {
            when (it.status) {
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

        viewModel.addBookResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    bottomSheetDialog?.dismiss()
                    hideProgress()
                    successAlert("Book successfully added")
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
        toast(message)
    }

    override fun onShelfOptionClicked(shelf: Shelf) {
        book?.let { viewModel.addBookIntoShelf(it, shelf) }
    }

    private fun showShelvesDialog(shelves: List<Shelf>) {
        val sheetView =
            requireActivity().layoutInflater.inflate(R.layout.bottom_sheet_options_dialog, null)
        bottomSheetDialog = createBottomSheetDialog(sheetView, "Add this book into shelf")

        val adapter = ShelfOptionsAdapter(requireContext(), this)
        adapter.add(shelves)

        val recyclerView = sheetView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        bottomSheetDialog?.show()
    }
}