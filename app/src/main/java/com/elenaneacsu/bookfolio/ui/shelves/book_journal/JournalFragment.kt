package com.elenaneacsu.bookfolio.ui.shelves.book_journal

//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context.CAMERA_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCapture.FLASH_MODE_AUTO
import androidx.core.content.ContextCompat
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class JournalFragment : BaseMvvmFragment<JournalViewModel, FragmentFavouritesBinding>(
    R.layout.fragment_favourites, JournalViewModel::class.java
) {

    private var quotesAdapter: QuotesAdapter? = null


    private var imageCapture: ImageCapture? = null

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

        // Request camera permissions
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            activity?.let {
//                ActivityCompat.requestPermissions(
//                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//            }
//        }

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
//                dispatchTakePictureIntent()
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                toast(
                    "Permissions not granted by the user.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            var image: InputImage? = null
            try {
                image = InputImage.fromFilePath(requireContext(), data.data!!)

                val recognizer = TextRecognition.getClient()
                val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // Task completed successfully
                        // ...
                        processTextRecognitionResult(visionText)
//                    val resultText = visionText.text
//                    for (block in visionText.textBlocks) {
//                        Log.d("TAG", "ml text: "+block.text)
//                        val blockText = block.text
//                        val blockCornerPoints = block.cornerPoints
//                        val blockFrame = block.boundingBox
//                        for (line in block.lines) {
//                            val lineText = line.text
//                            val lineCornerPoints = line.cornerPoints
//                            val lineFrame = line.boundingBox
//                            for (element in line.elements) {
//                                val elementText = element.text
//                                val elementCornerPoints = element.cornerPoints
//                                val elementFrame = element.boundingBox
//                            }
//                        }
//                    }
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            //load image to ml
//            imageView.setImageBitmap(imageBitmap)


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
//                for (k in elements.indices) {
//                    Log.d("TAG", "ml text: "+)
//                }
                for(el in elements) {
                    Log.d("TAG", "ml text: "+el.text)
                }
            }
        }
    }

    private fun showDialogToAddQuote() {
        context?.let { ctx ->

            val alertDialog = ctx.createCustomDialog(cancelable = true, style = R.style.AlertDialogStyle) {

                positiveButton("Save") {

                }

                negativeButton("Cancel") {

                }
            }

            val dialogView = alertDialog.layoutInflater.inflate(R.layout.add_quote_dialog_layout, null)
            alertDialog.setView(dialogView)
            dialogView.findViewById<ImageView>(R.id.take_photo_icon)?.setOnOneOffClickListener {
                dispatchTakePictureIntent()
            }

            alertDialog.show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { ctx ->
            ContextCompat.checkSelfPermission(
                ctx, it)
        } == PackageManager.PERMISSION_GRANTED
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
//        if (allPermissionsGranted()) {
//            startCamera()
//        } else {
//            activity?.let {
//                ActivityCompat.requestPermissions(
//                    it, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
//            }
//        }
    }


    private fun buildImageCaptureUseCase() {
        imageCapture =  ImageCapture.Builder()
//            .setTargetAspectRatio(aspectRatio)
//            .setTargetRotation(rotation)
//            .setTargetResolution(resolution)
            .setFlashMode(FLASH_MODE_AUTO)
            .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
    }

    fun buildPreviewUseCase(): Preview {
        return Preview.Builder()
            // Set the preview resolution
//            .setTargetResolution(resolution)
            // Set the preview aspect ratio. The resolution and aspect ratio cannot both be set tat the same time.
//            .setTargetAspectRatio()
            // Set the rotation the preview frames should be received in. This should typically be the display rotation.
//            .setTargetRotation(0)
            .build()
    }

    private fun startCamera() {
//        buildImageCaptureUseCase()
//        val preview = buildPreviewUseCase()
//        val surfaceProvider = previewView.createSurfaceProvider(cameraInfo)
//
//// Attach the SurfaceProvider to the preview to start displaying the camera preview.
//        preview.setSurfaceProvider(surfaceProvider)
//
////        val cameraProviderFuture = context?.let { ProcessCameraProvider.getInstance(it) }
////
////        cameraProviderFuture?.addListener(Runnable {
////            // Used to bind the lifecycle of cameras to the lifecycle owner
////            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
////
////            // Preview
////            val preview = Preview.Builder()
////                .build()
////                .also {
////                    it.setSurfaceProvider(viewBinding.cameraView.surfaceProvider)
////                }
////
////            imageCapture = ImageCapture.Builder()
////                .build()
////
////            // Select back camera as a default
////            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
////
////            try {
////                // Unbind use cases before rebinding
////                cameraProvider.unbindAll()
////
////                // Bind use cases to camera
////                cameraProvider.bindToLifecycle(
////                    this, cameraSelector, preview, imageCapture)
////
////            } catch(exc: Exception) {
////                Log.e(TAG, "Use case binding failed", exc)
////            }
////
////        }, ContextCompat.getMainExecutor(context))
//
//        imageCapture?.takePicture(ContextCompat.getMainExecutor(context), object: ImageCapture.OnImageCapturedCallback() {
//            override fun onCaptureSuccess(imageProxy: ImageProxy) {
//                // Use the image, then make sure to close it.
//                val img = InputImage.fromMediaImage(imageProxy.image, imageProxy.imageInfo.rotationDegrees)
//
//                val result = recognizer.process(img)
//                    .addOnSuccessListener { visionText ->
//                        // Task completed successfully
//                        processTextRecognitionResult(visionText)
//                    }
//                    .addOnFailureListener { e ->
//                        // Task failed with an exception
//                        // ...
//                    }
//                imageProxy.close()
//            }
//
//            override fun onError(exception: ImageCaptureException) {
//                val errorType = exception.imageCaptureError
//                errorAlert(errorType.toString())
//            }
//        })
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val MY_CAMERA_ID = "my_camera_id"
    }

    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(cameraId: String, activity: Activity, isFrontFacing: Boolean): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        // Get the device's sensor orientation.
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        }
        return rotationCompensation
    }

}