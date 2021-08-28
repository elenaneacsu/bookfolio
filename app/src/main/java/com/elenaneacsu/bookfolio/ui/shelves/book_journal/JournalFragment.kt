package com.elenaneacsu.bookfolio.ui.shelves.book_journal

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentFavouritesBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.ui.favourites.QuotesAdapter
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class JournalFragment : BaseMvvmFragment<JournalViewModel, FragmentFavouritesBinding>(
    R.layout.fragment_favourites, JournalViewModel::class.java
) {

    private var quotesAdapter: QuotesAdapter? = null
    private var currentPhotoFile: File? = null

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
            quotesAdapter = QuotesAdapter(it)
        }

        viewBinding.apply {

            toolbar.apply {
                setNavigationOnClickListener {
                    (activity as? MainActivity)?.onSupportNavigateUp()
                }
                title = activity?.getString(R.string.book_journal_title)
            }

            quotesRecyclerView.adapter = quotesAdapter

            temporaryFab.setOnOneOffClickListener {
                showDialogToAddQuote()
            }
        }
    }

    override fun hideProgress() {

    }

    override fun showProgress() {
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                val image =
                    currentPhotoFile?.toUri()?.let {
                        InputImage.fromFilePath(
                            requireContext(),
                            it
                        )
                    } ?: run {
                        logDebug("mare chestie null la currentPhotoFile")
                        null
                    }

                image?.let {
                    val recognizer = TextRecognition.getClient()
                    val result = recognizer.process(it)
                        .addOnSuccessListener { visionText ->
                            processTextRecognitionResult(visionText)
                        }
                        .addOnFailureListener { e ->
                            logError(e.message, e)
                        }
                } ?: logDebug("mare chestie null la image")
            }
        }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            toast("No text found")
            Log.d("TAG", "ml text: No text found")
            return
        }
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].lines
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].elements
                for (el in elements) {
                    Log.d("TAG", "ml text: " + el.text)
                }
            }
        }
    }

    private fun showDialogToAddQuote() {
        context?.let { ctx ->

            val alertDialog =
                ctx.createCustomDialog(cancelable = true, style = R.style.AlertDialogStyle) {

                    positiveButton("Save") {

                    }

                    negativeButton("Cancel") {

                    }
                }

            val dialogView =
                alertDialog.layoutInflater.inflate(R.layout.add_quote_dialog_layout, null)
            alertDialog.setView(dialogView)
            dialogView.findViewById<ImageView>(R.id.take_photo_icon)?.setOnOneOffClickListener {
                requireContext().requestCameraPermission(object : PermissionsCallback {
                    override fun onPermissionRequest(granted: Boolean) {
                        if (granted) {
                            dispatchTakePictureIntent()
                        }  else {
                            toast("Permissions not granted by the user.")
                        }
                    }
                })
            }

            alertDialog.show()
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

    fun Context.createImageFile(imageFileName: String): File? {
        return try {
            val parent = File(cacheDir, "images")
            parent.mkdirs()
            val file = File(parent, imageFileName)
            Log.d("IMAGE", "createImageFile - ${file.absolutePath}")
            if (file.exists())
                file.delete()
            file
        } catch (e: Exception) {
            null
        }
    }


    fun Context.requestCameraPermission(callback: PermissionsCallback) {
        requestSinglePermission(Manifest.permission.CAMERA, callback)
    }


    fun Context.requestSinglePermission(permission: String, callback: PermissionsCallback) {
        Dexter.withContext(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // User has granted the permission
                    callback.onPermissionRequest(granted = true)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    // User previously denied the permission, request them again
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    // User has denied the permission
                    callback.onPermissionRequest(granted = false)
                }
            })
            .check()
    }

    companion object {
        const val TAKE_PHOTO_REQUEST_CODE = 8001
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}


interface PermissionsCallback {

    // Pass request granted status i.e true or false
    fun onPermissionRequest(granted: Boolean)

}