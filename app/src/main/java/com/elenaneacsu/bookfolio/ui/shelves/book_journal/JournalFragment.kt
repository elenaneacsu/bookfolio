package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentFavouritesBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.models.BookDetailsMapper
import com.elenaneacsu.bookfolio.models.BookJournal
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.models.Shelf
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.favourites.QuotesAdapter
import com.elenaneacsu.bookfolio.utils.Constants
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class JournalFragment : QuotesAdapter.OnItemClickListener,
    BaseMvvmFragment<JournalViewModel, FragmentFavouritesBinding>(
        R.layout.fragment_favourites, JournalViewModel::class.java
    ) {

    private var quotesAdapter: QuotesAdapter? = null
    private var currentPhotoFile: File? = null
    private var addQuoteAlertDialog: AlertDialog? = null
    private var addQuoteDialogView: View? = null

    private var shelf: Shelf? = null
    private var book: BookDetailsMapper? = null
    private var journal: BookJournal? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (activity as? MainActivity)?.apply {
            updateStatusBarColor(R.color.primary, false)
            viewBinding.pullToRefresh.setColorSchemeColors(getThemeColor(R.attr.colorAccent))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments ?: return

        val args = JournalFragmentArgs.fromBundle(bundle)

        shelf = args.shelf
        book = args.book
        viewBinding.book = book
        journal = args.journal

        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViews() {
        super.initViews()

        context?.let {
            quotesAdapter = QuotesAdapter(it, this)
        }

        viewBinding.apply {

            toolbar.apply {
                setNavigationOnClickListener {
                    (activity as? MainActivity)?.onSupportNavigateUp()
                }
                title = activity?.getString(R.string.book_journal_title)
            }

            pullToRefresh.setOnRefreshListener {
                viewModel.getBookJournal(shelf, this@JournalFragment.book)
            }

            quotesRecyclerView.adapter = quotesAdapter
            journal?.quotes?.let { quotes ->
                quotesAdapter?.add(quotes)
                quotesAdapter?.notifyDataSetChanged()
            }

            temporaryFab.setOnOneOffClickListener {
                showDialogToAddQuote()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.addQuoteToJournalResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { quote ->
                        quotesAdapter?.addQuote(quote)
                        quotesAdapter?.itemCount?.let { numberOfQuotes ->
                            viewBinding.quotesRecyclerView.smoothScrollToPosition(numberOfQuotes - 1)
                        }
                    }
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })

        viewModel.bookJournalResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    it.data?.quotes?.let { quotes ->
                        quotesAdapter?.add(quotes)
                        quotesAdapter?.notifyDataSetChanged()
                    }
                    hideProgress()
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })

        viewModel.updateQuoteResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    it.data?.let { quotesPair ->
                        quotesAdapter?.updateQuote(
                            quotesPair.first,
                            quotesPair.second
                        )
                    }
                    hideProgress()
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }

    override fun hideProgress() {
        viewBinding.pullToRefresh.isRefreshing = false
    }

    override fun showProgress() {
        viewBinding.pullToRefresh.isRefreshing = true
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                val image =
                    currentPhotoFile?.toUri()?.let {
                        InputImage.fromFilePath(
                            requireContext(),
                            it
                        )
                    } ?: run {
                        logDebug("null la currentPhotoFile")
                        null
                    }

                image?.let {
                    val recognizer = TextRecognition.getClient()
                    recognizer.process(it)
                        .addOnSuccessListener { visionText ->
                            val text = processTextRecognitionResult(visionText)
                            addQuoteDialogView?.findViewById<TextInputEditText>(R.id.quote)
                                ?.setText(text)
                        }
                        .addOnFailureListener { e ->
                            logError(e.message, e)
                        }
                } ?: logDebug("null la image")
            }
        }
    }

    override fun onPageIconClicked(quote: Quote) {
        context?.let { ctx ->
            val editText = EditText(ctx).apply { inputType = InputType.TYPE_CLASS_NUMBER }
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Add a page number")
                setView(editText)
                positiveButton("Save") {
                    viewModel.updateQuoteData(shelf, book, quote, editText.text?.toString())
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

    private fun processTextRecognitionResult(texts: Text): String {
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            toast("No text found")
            Log.d("TAG", "ml text: No text found")
            return Constants.EMPTY
        }
        val textResult = StringBuilder()
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].lines
            for (line in lines) {
                if (lines.indexOf(line) == lines.size - 1)
                    textResult.append(line.text)
                else
                    textResult.append(line.text + "\n")
            }
        }
        return textResult.toString()
    }

    private fun showDialogToAddQuote() {
        context?.let { ctx ->

            addQuoteAlertDialog =
                ctx.createCustomDialog(cancelable = false, style = R.style.AlertDialogStyle) {

                    positiveButton("Save") {
                        val text =
                            addQuoteDialogView?.findViewById<TextInputEditText>(R.id.quote)?.text?.toString()
                        viewModel.addQuoteToJournal(shelf, book, text)
                    }

                    negativeButton("Cancel") {}
                }

            addQuoteDialogView =
                addQuoteAlertDialog!!.layoutInflater.inflate(R.layout.add_quote_dialog_layout, null)
            addQuoteAlertDialog!!.setView(addQuoteDialogView)
            addQuoteDialogView!!.findViewById<ImageView>(R.id.take_photo_icon)
                ?.setOnOneOffClickListener {
                    requireContext().requestCameraPermission(object : PermissionsCallback {
                        override fun onPermissionRequest(granted: Boolean) {
                            if (granted) {
                                dispatchTakePictureIntent()
                            } else {
                                toast("Permissions not granted by the user.")
                            }
                        }
                    })
                }

            addQuoteAlertDialog!!.show()
        }
    }

    private fun dispatchTakePictureIntent() {
        currentPhotoFile = null
        activity?.let {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(it.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = it.createImageFile("profile_${SystemClock.elapsedRealtime()}.jpg")
                    currentPhotoFile = photoFile
                } catch (ex: IOException) {
                    logError(ex.message, ex)
                }

                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        it,
                        "com.elenaneacsu.bookfolio",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE)
                }
            }
        }
    }

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 8001
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}