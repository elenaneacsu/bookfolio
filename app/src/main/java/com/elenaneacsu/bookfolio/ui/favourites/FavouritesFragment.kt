package com.elenaneacsu.bookfolio.ui.favourites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.text.InputType
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
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.models.QuoteKey
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.shelves.book_journal.JournalFragment
import com.elenaneacsu.bookfolio.ui.shelves.book_journal.JournalViewModel
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

/**
 * Created by Elena Neacsu on 13/06/21
 */
@AndroidEntryPoint
class FavouritesFragment : QuotesAdapter.OnItemClickListener,
    BaseMvvmFragment<JournalViewModel, FragmentFavouritesBinding>(
        R.layout.fragment_favourites, JournalViewModel::class.java
    ) {

    private var quotesAdapter: QuotesAdapter? = null
    private var currentPhotoFile: File? = null
    private var addQuoteAlertDialog: AlertDialog? = null
    private var addQuoteDialogView: View? = null

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


    override fun initViews() {
        super.initViews()

        context?.let {
            quotesAdapter = QuotesAdapter(it, this)
        }

        viewBinding.apply {
            toolbar.navigationIcon = null
            toolbar.subtitle = null

            quotesRecyclerView.adapter = quotesAdapter

            temporaryFab.visibility = View.GONE
        }
    }

    override fun initObservers() {
        viewModel.favouriteQuotes.observe(viewLifecycleOwner, {
            quotesAdapter?.add(it.map { dbQuote ->
                Quote(
                    dbQuote.id,
                    dbQuote.text,
                    dbQuote.book,
                    dbQuote.author,
                    dbQuote.page,
                    dbQuote.date,
                    true
                )
            })
            quotesAdapter?.notifyDataSetChanged()
        })

        viewModel.removeQuoteResult.observe(viewLifecycleOwner, {
            when (it.status) {
                Result.Status.LOADING -> showProgress()
                Result.Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { quote ->
                        activity?.getString(R.string.removed_quote_from_favourites_success)
                            ?.let { message ->
                                successAlert(
                                    message
                                )
                            }
                    }
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
                    hideProgress()
                    it.data?.let { quotesPair ->
                        quotesAdapter?.updateQuote(
                            quotesPair.first,
                            quotesPair.second
                        )
                    }
                    toast("Quote updated successfully.")
                }
                Result.Status.ERROR -> {
                    hideProgress()
                    errorAlert(it.message ?: getString(R.string.default_error_message))
                }
            }
        })
    }

    override fun hideProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = false
            viewOverlay.visibility = View.GONE
        }
    }

    override fun showProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = true
            viewOverlay.visibility = View.VISIBLE
        }
    }

    override fun errorAlert(message: String) {
        toast(message)
    }

    override fun successAlert(message: String) {
        toast(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == JournalFragment.TAKE_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
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
                                ?.append(text)
                        }
                        .addOnFailureListener { e ->
                            logError(e.message, e)
                        }
                } ?: logDebug("null la image")
            }
        }
    }

    override fun onQuoteFullTextClicked(quote: Quote) {
        context?.let { ctx ->

            addQuoteAlertDialog =
                ctx.createCustomDialog(cancelable = true, style = R.style.AlertDialogStyle) {

                    positiveButton("Save") {
                        val text =
                            addQuoteDialogView?.findViewById<TextInputEditText>(R.id.quote)?.text?.toString()
                        viewModel.updateQuoteData(quote = quote, key = QuoteKey.TEXT, value = text)
                    }

                    negativeButton("Cancel") {}
                }

            addQuoteDialogView =
                addQuoteAlertDialog!!.layoutInflater.inflate(R.layout.add_quote_dialog_layout, null)
            addQuoteAlertDialog!!.setView(addQuoteDialogView)
            addQuoteDialogView!!.apply {
                findViewById<ImageView>(R.id.take_photo_icon)
                    ?.setOnOneOffClickListener {
                        requireCameraPermission()
                    }
                findViewById<TextInputEditText>(R.id.quote)?.setText(quote?.text)
            }

            addQuoteAlertDialog!!.show()
        }
    }

    override fun onPageIconClicked(quote: Quote) {
        context?.let { ctx ->
            val editText = EditText(ctx).apply { inputType = InputType.TYPE_CLASS_NUMBER }
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Add a page number")
                setView(editText)
                positiveButton("Save") {
                    viewModel.updateQuoteData(
                        quote = quote,
                        key = QuoteKey.PAGE,
                        value = editText.text?.toString()
                    )
                }
                negativeButton("Cancel") {
                    it.dismiss()
                }
            }
        }
    }

    override fun onDateIconClicked(quote: Quote) {
        showMaterialDatePicker(false, quote.date, {
            it.dismiss()
        }, {
            viewModel.updateQuoteData(quote = quote, key = QuoteKey.DATE, value = it)
        })
    }

    override fun onRemoveQuoteClicked(quote: Quote) {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setTitle("Remove from favourites")
                setMessage("Are you sure you want to remove this quote from your favourites?")
                positiveButton {
                    viewModel.removeQuoteFromFavourites(quote = quote)
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
                    startActivityForResult(
                        takePictureIntent,
                        JournalFragment.TAKE_PHOTO_REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun requireCameraPermission() {
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
}